package com.vb.phoenix.rabbit.producer;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.vb.phoenix.pedido.domain.PedidoModel;
import com.vb.phoenix.pedido.domain.PedidoRabbit;
import com.vb.phoenix.usuario.domain.EmailRabbit;
import com.vb.phoenix.usuario.domain.UsuarioModel;

@Component
public class UsuarioProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${broker.queue.pedido.name}")
    private String pedidoRoutingKey;

    @Value("${broker.queue.email.name}")
    private String emailRoutingKey;

    public UsuarioProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishMessageEmail(UsuarioModel usuarioModel) {
        try {
            var emailRabbit = new EmailRabbit();
            emailRabbit.setUserId(usuarioModel.getId());
            emailRabbit.setEmailTo(usuarioModel.getEmail());
            emailRabbit.setSubject("Cadastro realizado com sucesso!");
            emailRabbit.setText(usuarioModel.getNome() +
                " seja bem vindo! \nAgradecemos o seu cadastro para utilizar da nossa plataforma!");

            rabbitTemplate.convertAndSend("", emailRoutingKey, emailRabbit);
            System.out.println("Email de cadastro enviado para fila: " + usuarioModel.getEmail());
        } catch (Exception e) {
            System.err.println("Erro ao enviar notificação de email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void publishMessagePedidoCriado(PedidoModel pedidoModel) {
        try {
            var pedidoRabbit = new PedidoRabbit();
            pedidoRabbit.setPedidoId(pedidoModel.getId());
            pedidoRabbit.setUsuarioId(pedidoModel.getUsuarioModel().getId());
            pedidoRabbit.setNomeUsuario(pedidoModel.getUsuarioModel().getNome());
            pedidoRabbit.setEmailUsuario(pedidoModel.getUsuarioModel().getEmail());
            pedidoRabbit.setValorTotal(pedidoModel.getValorTotal());
            pedidoRabbit.setStatus(pedidoModel.getStatus().toString());
            pedidoRabbit.setDataPedido(LocalDateTime.now());
            pedidoRabbit.setQuantidadeItens(pedidoModel.getProdutoModels().size());

            rabbitTemplate.convertAndSend("", pedidoRoutingKey, pedidoRabbit);
            System.out.println("Notificação de pedido enviada para fila: " + pedidoModel.getId());
        } catch (Exception e) {
            System.err.println("Erro ao enviar notificação do pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }
}