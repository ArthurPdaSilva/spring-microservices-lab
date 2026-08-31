# Arquitetura

## Visao Geral

O monorepo reune dois blocos de estudo:

- Microsservicos Spring Cloud do curso da Udemy, com Config Server, Greeting Service, Exchange Service e Book Service.
- Fluxo usuario-email inspirado no video do YouTube, com dois servicos Spring Boot integrados por RabbitMQ.

## Bloco Spring Cloud

```text
config-repo/spring-config-server
          |
          v
spring-cloud-config-server :8888
          |
          v
greeting-service :8080

book-service-microsservice :8100 ---> exchange-service-microsservice :8000 ---> MySQL
```

### Spring Cloud Config Server

Responsavel por expor configuracoes externas para os servicos. O repositorio de configuracao fica em `config-repo/spring-config-server` e pode ser substituido por uma URL remota via `CONFIG_REPO_URI`.

### Greeting Service

Servico REST simples que consome configuracoes do Config Server e expoe:

- `GET /greeting`
- `GET /greeting/properties?greeting=Hello&defaultValue=World`

### Exchange Service

Servico REST com MySQL e Flyway para consulta de cambio. Expoe:

- `GET /exchange-service?amount=10&from=USD&to=BRL`

### Book Service

Servico REST que busca livros e consulta o Exchange Service para retornar o preco convertido. Expoe:

- `GET /book-service/{id}/{currency}`

## Bloco Usuario-Email

```text
POST /users
  |
  v
user service ---> PostgreSQL
  |
  v
RabbitMQ queue
  |
  v
email service ---> SMTP
  |
  v
PostgreSQL
```

### User Service

Recebe o cadastro de usuario, persiste no PostgreSQL e publica uma mensagem na fila de email.

Endpoint principal:

- `POST /users`

### Email Service

Consome mensagens da fila configurada em `broker.queue.email.name`, tenta enviar o email e salva o status do envio.

## Decisoes de Organizacao

- Cada servico permanece independente para preservar o formato original dos estudos.
- O monorepo centraliza documentacao, infraestrutura local e referencias.
- Artefatos gerados e arquivos locais sensiveis nao foram copiados para o novo repositorio.
