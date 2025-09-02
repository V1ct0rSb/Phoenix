package com.vb.phoenix.usuario.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vb.phoenix.usuario.dto.UsuarioCreateDTO;
import com.vb.phoenix.usuario.dto.UsuarioResponseDTO;
import com.vb.phoenix.usuario.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioResponseDTO> logarUsuario(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.logarUsuario(usuarioCreateDTO);
        return ResponseEntity.status(201).body(usuarioResponseDTO);
    }
}
