// ItemPedidoRequestDTO.java  
package com.vb.phoenix.pedido.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemPedidoRequestDTO(

    @NotNull(message = "ID do produto é obrigatório")
    UUID produtoId,

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    Integer quantidade

) {}