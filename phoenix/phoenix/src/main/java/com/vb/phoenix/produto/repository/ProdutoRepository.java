package com.vb.phoenix.produto.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vb.phoenix.produto.domain.ProdutoModel;

public interface ProdutoRepository extends JpaRepository<ProdutoModel, UUID> {


    Optional<ProdutoModel> findByNome(String nome);
}
