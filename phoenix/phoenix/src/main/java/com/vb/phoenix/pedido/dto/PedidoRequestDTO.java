// PedidoRequestDTO.java
package com.vb.phoenix.pedido.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PedidoRequestDTO(

    @NotNull(message = "ID do usuário é obrigatório")
    UUID usuarioId,

    @NotNull(message = "Lista de itens é obrigatória")
    @NotEmpty(message = "Pedido deve conter pelo menos um item")
    @Valid
    List<ItemPedidoRequestDTO> itens

) {}