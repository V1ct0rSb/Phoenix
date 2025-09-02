package com.vb.email.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PedidoRecordDTO(
    UUID pedidoId,
    UUID usuarioId,
    String nomeUsuario,
    String emailUsuario,
    BigDecimal valorTotal,
    String status,
    LocalDateTime dataPedido,
    Integer quantidadeItens
) {}