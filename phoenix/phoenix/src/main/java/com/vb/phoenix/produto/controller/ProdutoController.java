package com.vb.phoenix.produto.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vb.phoenix.produto.domain.ProdutoModel;
import com.vb.phoenix.produto.dto.ProdutoCreateDTO;
import com.vb.phoenix.produto.service.ProdutoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class ProdutoController {
    private final ProdutoService produtoService;

    @PostMapping("produtos")
    public ResponseEntity<ProdutoModel> addProduto(@RequestBody ProdutoCreateDTO produtoCreateDTO) {
        ProdutoModel produtoModel = produtoService.addProduto(produtoCreateDTO);
        return ResponseEntity.status(201).body(produtoModel);
    }

    @GetMapping("produtos")
    public ResponseEntity<List<ProdutoModel>> listarProdutos() {
        return ResponseEntity.ok().body(produtoService.listarProdutos());
    }

}
