package com.vb.phoenix.pedido.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vb.phoenix.pedido.domain.PedidoModel;
import com.vb.phoenix.pedido.dto.ItemPedidoRequestDTO;
import com.vb.phoenix.pedido.dto.PedidoRequestDTO;
import com.vb.phoenix.pedido.enums.StatusPedido;
import com.vb.phoenix.pedido.repository.PedidoRepository;
import com.vb.phoenix.produto.domain.ProdutoModel;
import com.vb.phoenix.produto.repository.ProdutoRepository;
import com.vb.phoenix.rabbit.producer.UsuarioProducer;
import com.vb.phoenix.usuario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioProducer usuarioProducer;

    @Transactional
    public PedidoModel cadastrarPedido(PedidoRequestDTO pedidoRequestDTO) {

        var usuario = usuarioRepository.findById(pedidoRequestDTO.usuarioId())
                                       .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));


        if (pedidoRequestDTO.itens() == null || pedidoRequestDTO.itens().isEmpty()) {
            throw new RuntimeException("Pedido deve conter pelo menos um item.");
        }

        BigDecimal valorTotal = BigDecimal.ZERO;
        Set<ProdutoModel> produtosDoPedido = new HashSet<>();
        Map<ProdutoModel, Integer> produtosEQuantidades = new HashMap<>();

        for (ItemPedidoRequestDTO itemDTO : pedidoRequestDTO.itens()) {

            if (itemDTO.quantidade() <= 0) {
                throw new RuntimeException("Quantidade deve ser maior que zero.");
            }

            ProdutoModel produto = produtoRepository.findById(itemDTO.produtoId())
                                                    .orElseThrow(() -> new RuntimeException(
                                                        "Produto não encontrado: " + itemDTO.produtoId()));

            if (produto.getQuantidadeEstoque() < itemDTO.quantidade()) {
                throw new RuntimeException(
                    "Estoque insuficiente para o produto: " + produto.getNome() + ". Estoque disponível: " +
                        produto.getQuantidadeEstoque() + ", quantidade solicitada: " + itemDTO.quantidade());
            }

            produtosDoPedido.add(produto);
            produtosEQuantidades.put(produto, itemDTO.quantidade());

            BigDecimal quantidade = new BigDecimal(itemDTO.quantidade());
            BigDecimal subtotal = produto.getPreco().multiply(quantidade);
            valorTotal = valorTotal.add(subtotal);
        }

        var pedido = new PedidoModel();
        pedido.setUsuarioModel(usuario);
        pedido.setValorTotal(valorTotal);
        pedido.setStatus(StatusPedido.PAGO);
        pedido.setProdutoModels(produtosDoPedido);

        PedidoModel pedidoSalvo = pedidoRepository.save(pedido);

        for (Map.Entry<ProdutoModel, Integer> entry : produtosEQuantidades.entrySet()) {
            ProdutoModel produto = entry.getKey();
            Integer quantidadeComprada = entry.getValue();

            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidadeComprada);
            produtoRepository.save(produto);

            System.out.println("Estoque atualizado - Produto: " + produto.getNome() + ", Novo estoque: " +
                produto.getQuantidadeEstoque());
        }

        try {
            usuarioProducer.publishMessagePedidoCriado(pedidoSalvo);
        } catch (Exception e) {
            System.err.println("Erro ao enviar notificação do pedido: " + e.getMessage());

        }

        return pedidoSalvo;
    }
}