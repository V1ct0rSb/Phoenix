package com.vb.phoenix.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioCreateDTO(@NotBlank(message = "O nome de usuário é obrigatorio!") String nome,
                               @NotBlank(message = "O email do usuário é obriagtorio!") @Email String email,
                               @NotBlank(message = "A senha do usuário é obrigatoria!") String senha) {
}
