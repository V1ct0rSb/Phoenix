package com.vb.phoenix.pedido.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vb.phoenix.pedido.domain.PedidoModel;

public interface PedidoRepository extends JpaRepository<PedidoModel, UUID> {
}
