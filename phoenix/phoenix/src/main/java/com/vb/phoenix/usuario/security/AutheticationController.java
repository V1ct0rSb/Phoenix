package com.vb.phoenix.usuario.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vb.phoenix.usuario.dto.UsuarioLoginRequestDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AutheticationController {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("login")
    public String authenticate(@RequestBody UsuarioLoginRequestDTO usarioLogado) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(
            usarioLogado.email(),
            usarioLogado.senha()
        );

        Authentication authenticated = authenticationManager.authenticate(authenticationToken);

        return jwtService.generateToken(authenticated);
    }
}
