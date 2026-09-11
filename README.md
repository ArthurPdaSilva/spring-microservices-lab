# Spring Microservices Lab

Laboratorio de arquitetura de microsservicos com Java 21, Spring Boot 4, Spring Cloud, API Gateway, Docker, Kubernetes e persistencia relacional.

O repositorio reune projetos de estudo antes separados e mantem cada aplicacao com `pom.xml` e Maven Wrapper independentes. A descoberta e o balanceamento entre instancias ficam sob responsabilidade dos Services do Kubernetes; no Compose local, os servicos usam os nomes DNS da rede Docker.

Repositorio: [github.com/ArthurPdaSilva/spring-microservices-lab](https://github.com/ArthurPdaSilva/spring-microservices-lab)

## Arquitetura Ativa

```text
Cliente --> API Gateway :8765 --> Book Service :8100 --> Exchange Service :8000
                                     |                         |
                                     +------ MySQL :3306 ------+
```

O fluxo principal recebe a chamada no Gateway, encaminha para o Book Service e consulta o Exchange Service via OpenFeign. As URLs usam defaults locais e podem ser substituidas pelos nomes DNS dos Services do Kubernetes.

## Stack

- Java 21, Spring Boot 4.1.1 e Spring Cloud 2025.1.3.
- Spring Cloud Gateway WebFlux e Spring Cloud OpenFeign.
- Spring Data JPA, MySQL 8.4 e Flyway.
- Spring Actuator para endpoints operacionais e health checks.
- Springdoc OpenAPI 3.1.0 com Swagger UI agregado no Gateway.
- Resilience4j no laboratorio do Book Service.
- Docker Compose e Maven Wrapper.
- PostgreSQL e RabbitMQ no fluxo complementar de usuario e email.

## Servicos Ativos no Compose

| Servico | Porta | Papel |
|---|---:|---|
| `api-gateway` | 8765 | Ponto de entrada, roteamento, balanceamento, CORS e Swagger agregado. |
| `book-service-microsservice` | 8100 | Consulta livros e calcula precos convertidos por meio do Exchange Service. |
| `exchange-service-microsservice` | 8000 | Consulta fatores de cambio e calcula valores convertidos. |
| `mysql` | 3306 | Persiste livros e taxas de cambio no schema compartilhado `microservice_section_7`. |

O Config Server, Greeting Service, PostgreSQL, RabbitMQ e o fluxo User/Email permanecem no monorepo, mas estao comentados no `docker-compose.yml` principal e nao sobem no comando padrao.

## Gateway e Rotas

O Gateway usa rotas declarativas e URLs configuraveis por `BOOK_SERVICE_URL` e `EXCHANGE_SERVICE_URL`:

| Rota externa | Destino | Transformacao |
|---|---|---|
| `/book-service-microsservice/**` | Book Service | Remove o primeiro segmento com `StripPrefix=1`. |
| `/book-service/**` | Book Service | Mantem o caminho curto. |
| `/exchange-service-microsservice/**` | Exchange Service | Remove o primeiro segmento com `StripPrefix=1`. |
| `/exchange-service/**` | Exchange Service | Mantem o caminho curto. |

Exemplos:

```bash
curl http://localhost:8765/book-service/2/BRL
curl "http://localhost:8765/exchange-service?amount=10&from=USD&to=BRL"
```

As respostas incluem o hostname e a porta das instancias envolvidas, permitindo identificar os Pods que atenderam cada trecho da requisicao no Kubernetes.

## Swagger e CORS

O Gateway agrega os documentos OpenAPI do Book e do Exchange Service. As URLs de servidor sao relativas aos prefixos do Gateway, evitando que o Swagger exponha hostnames internos do Docker.

- Swagger UI: [localhost:8765/swagger-ui.html](http://localhost:8765/swagger-ui.html)
- Book OpenAPI: [localhost:8765/book-service-microsservice/v3/api-docs](http://localhost:8765/book-service-microsservice/v3/api-docs)
- Exchange OpenAPI: [localhost:8765/exchange-service-microsservice/v3/api-docs](http://localhost:8765/exchange-service-microsservice/v3/api-docs)

O CORS global do Gateway permite todas as origens, headers e os metodos `GET`, `POST`, `PUT`, `PATCH`, `DELETE` e `OPTIONS`. Essa politica e adequada ao laboratorio local e deve ser restringida antes de uma exposicao publica.

## Persistencia

Book e Exchange usam o mesmo banco MySQL com tabelas de historico Flyway separadas:

- Book: `flyway_schema_history_book`, migration de criacao e carga inicial de livros.
- Exchange: `flyway_schema_history_exchange`, migration de criacao e carga inicial de taxas USD.
- Hibernate: `ddl-auto: none`; a estrutura e os dados iniciais ficam sob responsabilidade do Flyway.

## Resilience4j

O Book Service possui configuracoes de Retry, Rate Limiter e Bulkhead para o grupo `foo-bar`. No codigo atual, apenas o `Bulkhead` esta ativo no endpoint de demonstracao; Retry, Circuit Breaker, Rate Limiter e fallback permanecem como experimentos comentados.

## Modulos Complementares

### Config Server e Greeting

O `spring-cloud-config-server` suporta backend nativo e Git para servir as configuracoes de `config-repo/spring-config-server`. O `greeting-service` consome propriedades externas por perfil. Esses dois servicos sao executados separadamente no estado atual.

### User e Email

O fluxo assincrono implementa cadastro de usuario, persistencia em PostgreSQL, publicacao no RabbitMQ, consumo pelo Email Service, envio SMTP e persistencia do status. PostgreSQL, RabbitMQ, User e Email estao desativados no Compose principal, mas a infraestrutura pode ser iniciada pelo arquivo `docker-compose.infra.yml`.

## Estrutura

```text
spring-microservices-lab/
  config-repo/spring-config-server/    # Configuracoes externas por aplicacao e perfil
  docs/                                # Arquitetura e guia de execucao local
  services/
    api-gateway/                       # Gateway WebFlux e Swagger agregado
    book-service-microsservice/        # Livros, OpenFeign e Resilience4j
    exchange-service-microsservice/    # Cambio, JPA e Flyway
    spring-cloud-config-server/        # Config Server nativo/Git
    greeting-service/                  # Cliente do Config Server
    user-email-service/                # Fluxo assincrono User -> RabbitMQ -> Email
  Dockerfile                           # Build parametrizado dos servicos Java
  docker-compose.yml                   # Arquitetura principal ativa
  docker-compose.infra.yml             # MySQL, PostgreSQL e RabbitMQ para uso local
```

## Execucao Rapida

Pre-requisitos: Docker com Compose e portas `3306`, `8000`, `8100` e `8765` livres.

```bash
docker compose up -d --build
```

Verifique os containers:

```bash
docker compose ps
```

Interfaces principais:

- API Gateway: [localhost:8765](http://localhost:8765)
- Swagger UI: [localhost:8765/swagger-ui.html](http://localhost:8765/swagger-ui.html)

Para encerrar:

```bash
docker compose down
```

Use `docker compose down -v` somente quando tambem quiser remover os dados persistidos do MySQL.

## Execucao e Testes Individuais

Cada servico possui build independente. Entre no diretorio desejado e use seu Maven Wrapper:

```bash
cd services/book-service-microsservice
./mvnw test
./mvnw spring-boot:run
```

O `Dockerfile` empacota as aplicacoes com `-DskipTests`; execute os testes explicitamente durante o desenvolvimento.

Consulte o guia detalhado em [`docs/running-locally.md`](docs/running-locally.md), a visao arquitetural em [`docs/architecture.md`](docs/architecture.md) e a colecao [`spring-microservices-lab.postman_collection.json`](spring-microservices-lab.postman_collection.json).

## Referencias

- [Microservices do 0 a GCP com Spring Boot, Kubernetes e Docker](https://www.udemy.com/course/microservices-do-0-a-gcp-com-spring-boot-kubernetes-e-docker/), usado como referencia para o bloco Spring Cloud.
- [Microsservicos com Spring Boot e RabbitMQ](https://www.youtube.com/watch?v=ZnECi2gatMs&t=6161s), usado como referencia para o fluxo User/Email.

## Observacoes

- Este e um laboratorio educacional, nao um build Maven multi-module nem uma arquitetura pronta para producao.
- O CORS aberto foi escolhido para facilitar testes locais e deve ser restringido em producao.
- Credenciais reais devem permanecer em variaveis de ambiente ou arquivos `.env` locais nao versionados.
