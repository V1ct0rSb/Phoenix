package com.vb.email.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import com.vb.email.domain.EmailModel;
import com.vb.email.dto.EmailRecordDTO;
import com.vb.email.dto.PedidoRecordDTO;
import com.vb.email.service.EmailService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = "${broker.queue.email.name}")
    public void listenEmailQueue(@Payload EmailRecordDTO emailRecordDTO) {
        var emailModel = new EmailModel();
        emailModel.setUserId(emailRecordDTO.userId());
        emailModel.setEmailTo(emailRecordDTO.emailTo());
        emailModel.setSubject(emailRecordDTO.subject());
        emailModel.setText(emailRecordDTO.text());

        emailService.sendEmail(emailModel);
    }

    @RabbitListener(queues = "${broker.queue.pedido.name}")
    public void listenPedidoQueue(@Payload PedidoRecordDTO pedidoRecordDTO) {
        var emailModel = new EmailModel();
        emailModel.setEmailTo(pedidoRecordDTO.emailUsuario());
        emailModel.setSubject("Pedido #" + pedidoRecordDTO.pedidoId() + " confirmado!");
        emailModel.setText(
            "Olá " + pedidoRecordDTO.nomeUsuario() + "!\n\n" +
                "Seu pedido foi confirmado com sucesso!\n" +
                "Número do pedido: " + pedidoRecordDTO.pedidoId() + "\n" +
                "Valor total: R$ " + pedidoRecordDTO.valorTotal() + "\n" +
                "Status: " + pedidoRecordDTO.status() + "\n" +
                "Quantidade de itens: " + pedidoRecordDTO.quantidadeItens() + "\n\n" +
                "Obrigado pela sua compra!"
        );

        emailService.sendEmail(emailModel);
        System.out.println("Email de confirmação de pedido enviado para: " + pedidoRecordDTO.emailUsuario());
    }
}
