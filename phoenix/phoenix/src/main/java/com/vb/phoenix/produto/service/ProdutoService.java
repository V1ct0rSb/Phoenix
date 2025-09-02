package com.vb.phoenix.produto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vb.phoenix.produto.domain.ProdutoModel;
import com.vb.phoenix.produto.dto.ProdutoCreateDTO;
import com.vb.phoenix.produto.repository.ProdutoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Transactional
    public ProdutoModel addProduto(ProdutoCreateDTO produtoCreateDTO) {
        Optional<ProdutoModel> produtoModel = produtoRepository.findByNome(produtoCreateDTO.nome());
        ProdutoModel produtoFinal;

        if (produtoModel.isPresent()) {
            produtoFinal = produtoModel.get();
            produtoFinal.setPreco(produtoCreateDTO.preco());
            produtoFinal.setQuantidadeEstoque(
                produtoFinal.getQuantidadeEstoque() + produtoCreateDTO.quantidadeEstoque());
        } else {
            produtoFinal = new ProdutoModel();
            produtoFinal.setNome(produtoCreateDTO.nome());
            produtoFinal.setPreco(produtoCreateDTO.preco());
            produtoFinal.setQuantidadeEstoque(produtoCreateDTO.quantidadeEstoque());
        }
        return produtoRepository.save(produtoFinal);
    }

    public List<ProdutoModel> listarProdutos() {
        return produtoRepository.findAll();
    }
}
