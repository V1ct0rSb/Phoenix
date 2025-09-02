package com.vb.phoenix.usuario.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vb.phoenix.rabbit.producer.UsuarioProducer;
import com.vb.phoenix.usuario.domain.UsuarioModel;
import com.vb.phoenix.usuario.dto.UsuarioCreateDTO;
import com.vb.phoenix.usuario.dto.UsuarioResponseDTO;
import com.vb.phoenix.usuario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioProducer usuarioProducer;

    @Transactional
    public UsuarioResponseDTO logarUsuario(UsuarioCreateDTO usuarioCriado) {
        var usuario = new UsuarioModel();

        usuario.setNome(usuarioCriado.nome());
        usuario.setEmail(usuarioCriado.email());

        String senhaHash = passwordEncoder.encode(usuarioCriado.senha());
        usuario.setSenha(senhaHash);

        UsuarioModel usuarioSalvo = usuarioRepository.save(usuario);

        usuarioProducer.publishMessageEmail(usuarioSalvo);

        return new UsuarioResponseDTO(usuarioSalvo.getId(),
            usuarioSalvo.getNome(),
            usuarioSalvo.getEmail(),
            usuarioSalvo.getCriadoEm());
    }

}
