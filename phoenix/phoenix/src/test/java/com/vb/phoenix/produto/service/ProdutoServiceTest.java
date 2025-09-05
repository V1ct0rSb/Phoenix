package com.vb.phoenix.produto.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.vb.phoenix.produto.domain.ProdutoModel;
import com.vb.phoenix.produto.dto.ProdutoCreateDTO;
import com.vb.phoenix.produto.repository.ProdutoRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;
    @Mock
    private ProdutoRepository produtoRepository;

    private ProdutoCreateDTO produtoCreateDTO;
    private ProdutoModel produtoExistente;
    private ProdutoModel produtoSalvo;

    @BeforeEach
    void setUp() {
        // Dados de entrada (DTO)
        produtoCreateDTO = new ProdutoCreateDTO("Banana", BigDecimal.valueOf(200.00), 100);

        // Produto que será retornado pelo save
        produtoSalvo = new ProdutoModel();
        produtoSalvo.setNome("Banana");
        produtoSalvo.setPreco(BigDecimal.valueOf(200.00));
        produtoSalvo.setQuantidadeEstoque(100);

        // Produto que já existe no banco
        produtoExistente = new ProdutoModel();
        produtoExistente.setNome("Banana");
        produtoExistente.setPreco(BigDecimal.valueOf(150.00));
        produtoExistente.setQuantidadeEstoque(50);
    }

    // CENÁRIOS FELIZES (quando tudo dá certo)
    @Test
    @DisplayName("Deve adicionar novo produto quando não existe no banco")
    void deveAdicionarProdutoQuandoNaoExistir() {
        // GIVEN - Configurar comportamento dos mocks
        // WHEN -> Oq aconteceu | Oq espera
        when(produtoRepository.findByNome("Banana")).thenReturn(Optional.empty());
        when(produtoRepository.save(any(ProdutoModel.class))).thenReturn(produtoSalvo);

        // WHEN - Executar método testado
        ProdutoModel resultado = produtoService.addProduto(produtoCreateDTO);

        // THEN - Verificar resultado
        // Verificar se oq estavos esperando vai retornar oq realmente queremos
        assertNotNull(resultado);
        assertEquals("Banana", resultado.getNome());
        assertEquals(BigDecimal.valueOf(200.00), resultado.getPreco());
        assertEquals(100, resultado.getQuantidadeEstoque());
    }

    @Test
    void deveAtualizarProdutoQuandoJaExistir() {
        // GIVEN
        when(produtoRepository.findByNome("Banana")).thenReturn(Optional.of(produtoExistente));

        // Produto atualizado após a lógica do service
        ProdutoModel produtoAtualizado = new ProdutoModel();
        produtoAtualizado.setNome("Banana");
        produtoAtualizado.setPreco(BigDecimal.valueOf(200.00)); // Preço atualizado
        produtoAtualizado.setQuantidadeEstoque(150); // 50 + 100

        when(produtoRepository.save(any(ProdutoModel.class))).thenReturn(produtoAtualizado);

        // WHEN
        ProdutoModel resultado = produtoService.addProduto(produtoCreateDTO);

        // THEN
        assertNotNull(resultado);
        assertEquals("Banana", resultado.getNome());
        assertEquals(BigDecimal.valueOf(200.00), resultado.getPreco());
        assertEquals(150, resultado.getQuantidadeEstoque()); // Quantidade somada

        //Confirma a chamada aos moks pelo menos uma vez
        verify(produtoRepository, times(1)).findByNome("Banana");
        verify(produtoRepository, times(1)).save(any(ProdutoModel.class));
    }

    // CENÁRIOS DE ERRO/EXCEÇÃO (mundo real!)
    @Test
    void deveLancarExcecaoQuandoRepositoryFalhar() {
        // GIVEN - Simula erro no banco de dados
        when(produtoRepository.findByNome("Banana")).thenThrow(new RuntimeException("Erro de conexão com banco"));

        // WHEN & THEN - Verifica se a exceção é lançada
        assertThrows(RuntimeException.class, () -> {
            produtoService.addProduto(produtoCreateDTO);
        });

        verify(produtoRepository).findByNome("Banana");
        // save() não deve ser chamado se findByNome() falhar
        verify(produtoRepository, never()).save(any(ProdutoModel.class));
    }

    @Test
    void deveLancarExcecaoQuandoSaveFalhar() {
        // GIVEN - findByNome funciona, mas save() falha
        when(produtoRepository.findByNome("Banana")).thenReturn(Optional.empty());
        when(produtoRepository.save(any(ProdutoModel.class))).thenThrow(new RuntimeException("Erro ao salvar no banco"));

        // WHEN & THEN
        assertThrows(RuntimeException.class, () -> {
            produtoService.addProduto(produtoCreateDTO);
        });

        verify(produtoRepository).findByNome("Banana");
        verify(produtoRepository).save(any(ProdutoModel.class));
    }

    // CENÁRIOS EXTREMOS (edge cases)
    @Test
    void deveAdicionarProdutoComQuantidadeZero() {
        // GIVEN - Produto com quantidade zero
        ProdutoCreateDTO dtoQuantidadeZero = new ProdutoCreateDTO("Banana", BigDecimal.valueOf(200.00), 0);
        when(produtoRepository.findByNome("Banana")).thenReturn(Optional.empty());
        when(produtoRepository.save(any(ProdutoModel.class))).thenReturn(new ProdutoModel());

        // WHEN
        ProdutoModel resultado = produtoService.addProduto(dtoQuantidadeZero);

        // THEN
        assertNotNull(resultado);
        verify(produtoRepository).save(any(ProdutoModel.class));
    }

    @Test
    void deveAdicionarProdutoComPrecoZero() {
        // GIVEN - Produto com preço zero
        ProdutoCreateDTO dtoPrecoZero = new ProdutoCreateDTO("Banana", BigDecimal.ZERO, 100);
        when(produtoRepository.findByNome("Banana")).thenReturn(Optional.empty());
        when(produtoRepository.save(any(ProdutoModel.class))).thenReturn(new ProdutoModel());

        // WHEN
        ProdutoModel resultado = produtoService.addProduto(dtoPrecoZero);

        // THEN
        assertNotNull(resultado);
        verify(produtoRepository).save(any(ProdutoModel.class));
    }

    // 📝 CENÁRIOS DIFERENTES PARA LISTAR PRODUTOS
    @Test
    void deveListarProdutosQuandoTemProdutos() {
        // CENÁRIO FELIZ - tem produtos
        when(produtoRepository.findAll()).thenReturn(List.of(produtoExistente));

        List<ProdutoModel> resultado = produtoService.listarProdutos();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoTemProdutos() {
        // CENÁRIO ALTERNATIVO - não tem produtos
        when(produtoRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProdutoModel> resultado = produtoService.listarProdutos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty()); // Lista vazia, mas não null
        assertEquals(0, resultado.size());
    }

    @Test
    void deveLancarExcecaoQuandoListarProdutosFalhar() {
        // CENÁRIO DE ERRO - banco falha ao listar
        when(produtoRepository.findAll()).thenThrow(new RuntimeException("Erro ao conectar com banco"));

        assertThrows(RuntimeException.class, () -> {
            produtoService.listarProdutos();
        });
    }


}