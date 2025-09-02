package com.vb.phoenix.produto.dto;

import java.math.BigDecimal;

public record ProdutoCreateDTO(String nome, BigDecimal preco, Integer quantidadeEstoque) {
}
