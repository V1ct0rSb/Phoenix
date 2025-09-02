package com.vb.phoenix.pedido.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vb.phoenix.pedido.domain.PedidoModel;
import com.vb.phoenix.pedido.dto.PedidoRequestDTO;
import com.vb.phoenix.pedido.service.PedidoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/pedidos")
@Slf4j
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<?> cadastrarPedido(@Valid @RequestBody PedidoRequestDTO pedidoRequestDTO) {
        try {
            PedidoModel pedidoSalvo = pedidoService.cadastrarPedido(pedidoRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
        } catch (RuntimeException e) {
            log.error("Erro ao cadastrar pedido: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro interno ao cadastrar pedido: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ErrorResponse("Erro interno do servidor"));
        }
    }

    public static record ErrorResponse(String message) {}
}