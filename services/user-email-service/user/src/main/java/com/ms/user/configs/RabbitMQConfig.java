package com.ms.user.configs;

import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Aqui como ele é o producer, ele só precisa do messageConverter
    // Configuração de conversão: Recebendo um json e convertendo no dto
    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        JacksonJsonMessageConverter jsonConverter = new JacksonJsonMessageConverter();
        jsonConverter.setClassMapper(classMapper());
        return jsonConverter;
    }

    @Bean
    public DefaultClassMapper classMapper() {
        return new DefaultClassMapper();
    }
}
