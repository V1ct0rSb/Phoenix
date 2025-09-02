package com.vb.phoenix.usuario.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioResponseDTO(UUID id, String nome, String email, LocalDateTime criadoEm) {
}
