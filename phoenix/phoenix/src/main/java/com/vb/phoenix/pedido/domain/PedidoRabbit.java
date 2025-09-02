package com.vb.phoenix.pedido.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class PedidoRabbit {
    private UUID pedidoId;
    private UUID usuarioId;
    private String nomeUsuario;
    private String emailUsuario;
    private BigDecimal valorTotal;
    private String status;
    private LocalDateTime dataPedido;
    private Integer quantidadeItens;
}