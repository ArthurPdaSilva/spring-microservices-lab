# Executando Localmente

## Pre-requisitos

- Java 17 ou superior.
- Docker e Docker Compose.
- Maven Wrapper dos proprios projetos.

Alguns servicos usam Spring Boot 4.x e Java 21 nos `pom.xml`; use Java 21 se quiser rodar todos sem alternar versoes.

## Infraestrutura

Para subir todos os servicos e a infraestrutura pela raiz do monorepo:

```bash
docker compose up --build
```

Isso cria imagens para os servicos Spring Boot usando o `Dockerfile` raiz e sobe MySQL, PostgreSQL e RabbitMQ.

O `book-service` e o `exchange-service` usam `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://naming-server:8671/eureka/` no Compose para registrar no Eureka dentro da rede Docker. Sem essa variavel, o fallback `localhost:8671` aponta para o proprio container do servico, nao para o container do Eureka.

Se o MySQL ja tiver sido criado antes das migrations do Flyway, pode ocorrer erro de schema nao vazio sem tabela de historico. O `exchange-service` usa `baseline-on-migrate=true`, `baseline-version=0` e a tabela `flyway_schema_history_exchange` para conseguir iniciar nesse cenario. Se quiser reiniciar tudo do zero, remova os containers e volumes:

```bash
docker compose down -v
docker compose up --build
```

Se quiser executar os servicos manualmente pelo Maven, suba apenas bancos e RabbitMQ:

Suba bancos e RabbitMQ pela raiz do monorepo:

```bash
docker compose -f docker-compose.infra.yml up -d
```

Servicos expostos:

| Infra | Porta |
|---|---:|
| MySQL | 3306 |
| PostgreSQL | 5432 |
| RabbitMQ | 5672 |
| RabbitMQ Management | 15672 |

## Spring Cloud Config

Execute primeiro o Config Server:

```bash
cd services/spring-cloud-config-server
./mvnw spring-boot:run
```

Teste a configuracao do Greeting Service:

```bash
curl http://localhost:8888/greeting-service/it
```

Por padrao, o Config Server usa o perfil `native` e le as configuracoes locais em `config-repo/spring-config-server`.

Se quiser apontar para o repositorio remoto via Git:

```bash
SPRING_PROFILES_ACTIVE=default CONFIG_REPO_URI=git@github.com:ArthurPdaSilva/spring-microservices-lab.git CONFIG_REPO_LABEL=main ./mvnw spring-boot:run
```

O Config Server ja esta configurado para buscar os arquivos em `config-repo/spring-config-server` quando usa o repositorio remoto inteiro como fonte.

## Greeting Service

Com o Config Server ativo:

```bash
cd services/greeting-service
./mvnw spring-boot:run
```

Teste:

```bash
curl http://localhost:8080/greeting
curl "http://localhost:8080/greeting/properties?greeting=Hello&defaultValue=Arthur"
```

## Exchange Service

Com MySQL ativo:

```bash
cd services/exchange-service-microsservice
./mvnw spring-boot:run
```

Teste:

```bash
curl "http://localhost:8000/exchange-service?amount=10&from=USD&to=BRL"
```

## Book Service

Com MySQL e Exchange Service ativos:

```bash
cd services/book-service-microsservice
./mvnw spring-boot:run
```

Teste:

```bash
curl http://localhost:8100/book-service/1/BRL
```

## User Email Service

Crie arquivos `.env` locais ou exporte variaveis para `services/user-email-service/user` e `services/user-email-service/email`. Use os valores abaixo como base e ajuste SMTP antes de tentar envio real.

```env
SERVER_PORT=8081
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/user_email_service
SPRING_DATASOURCE_USERNAME=spring
SPRING_DATASOURCE_PASSWORD=spring
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_RABBITMQ_ADDRESSES=amqp://guest:guest@localhost:5672
BROKER_QUEUE_EMAIL_NAME=default.email
SPRING_MAIL_HOST=smtp.example.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=your-email@example.com
SPRING_MAIL_PASSWORD=your-password
SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH=true
SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE=true
```

Execute o User Service:

```bash
cd services/user-email-service/user
./mvnw spring-boot:run
```

Execute o Email Service em outro terminal:

```bash
cd services/user-email-service/email
SERVER_PORT=8082 ./mvnw spring-boot:run
```

Teste o cadastro:

```bash
curl -X POST http://localhost:8081/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Arthur","email":"arthur@example.com"}'
```

## Testes

Cada servico mantem seus testes independentes:

```bash
cd services/greeting-service
./mvnw test
```

Repita o comando dentro do servico que deseja validar.
