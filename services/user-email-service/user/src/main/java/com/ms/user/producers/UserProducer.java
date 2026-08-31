package com.ms.user.producers;

import com.ms.user.dtos.EmailDto;
import com.ms.user.models.UserModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {
    final RabbitTemplate rabbitTemplate;

    public UserProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${broker.queue.email.name}")
    private String routingKey;

    // Função responsável por publicar a mensagem para o consumer
    public void publicMessageEmail(UserModel userModel) {
        var emailDto = new EmailDto();
        emailDto.setUserId(userModel.getId());
        emailDto.setEmailTo(userModel.getEmail());
        emailDto.setSubject("Usuário cadastrado com sucesso!");
        emailDto.setText("Bem-vindo " + userModel.getName() + "!");
        // Como passei o exchange tá vazio, ele sabe que é o tipo exchange default: routing key
        rabbitTemplate.convertAndSend("", routingKey, emailDto);
    }

}
