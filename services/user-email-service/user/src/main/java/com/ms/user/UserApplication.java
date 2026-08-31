package com.ms.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserApplication {

    // Revisão de alguns conceitos:
    // Broker: Servidor de mensageria
    // Queue: Fila de mensagens, onde os produtores enviam mensagens e os consumidores as recebem
    // Exchange: Roteia as mensagens para as filas com base em regras de roteamento
    // Binding: Ligação entre uma exchange e uma fila, definindo as regras de roteamento
    // Routing Key: Chave usada para determinar para qual fila a mensagem deve ser roteada

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
