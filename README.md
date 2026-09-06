# Spring Microservices Lab

Laboratorio de arquitetura de microsservicos com Java 21, Spring Boot 4, Spring Cloud, service discovery, API Gateway, balanceamento de carga, observabilidade distribuida e persistencia relacional.

O repositorio reune projetos de estudo antes separados e mantem cada aplicacao com `pom.xml` e Maven Wrapper independentes. A composicao principal executa duas instancias dos servicos de livros e cambio para demonstrar descoberta e distribuicao de requisicoes em um ambiente Docker.

Repositorio: [github.com/ArthurPdaSilva/spring-microservices-lab](https://github.com/ArthurPdaSilva/spring-microservices-lab)

## Arquitetura Ativa

```text
                         +---------------------+
                         | Zipkin :9411        |
                         | tracing distribuido |
                         +----------^----------+
                                    |
Cliente --> API Gateway :8765 ------+------> Eureka :8671
               |                    |
               | lb://book-service-microsservice
               v
        +------+------+                 +-------------+
        |             |                 |             |
     Book :8100    Book :8101 -----> Exchange :8000  Exchange :8001
        |             |       OpenFeign/Eureka       |
        +------+------+                 +-------------+
               |                              |
               +------------ MySQL :3306 -----+
```

O fluxo principal recebe a chamada no Gateway, localiza uma instancia do Book Service pelo Eureka e distribui a requisicao entre as portas `8100` e `8101`. O Book consulta o livro no MySQL e chama o Exchange Service via OpenFeign e service discovery, com balanceamento entre as portas `8000` e `8001`.

## Stack

- Java 21, Spring Boot 4.1.1 e Spring Cloud 2025.1.3.
- Spring Cloud Gateway WebFlux e Netflix Eureka.
- Spring Cloud OpenFeign e Spring Cloud LoadBalancer.
- Spring Data JPA, MySQL 8.4 e Flyway.
- Spring Actuator e Micrometer Tracing com Brave.
- OpenZipkin Zipkin para coleta e visualizacao de traces.
- Springdoc OpenAPI 3.1.0 com Swagger UI agregado no Gateway.
- Resilience4j no laboratorio do Book Service.
- Docker Compose e Maven Wrapper.
- PostgreSQL e RabbitMQ no fluxo complementar de usuario e email.

## Servicos Ativos no Compose

| Servico | Porta | Papel |
|---|---:|---|
| `api-gateway` | 8765 | Ponto de entrada, roteamento, balanceamento, CORS e Swagger agregado. |
| `naming-server` | 8671 | Registro e descoberta de servicos com Eureka. |
| `book-service-microsservice` | 8100 e 8101 | Consulta livros e calcula precos convertidos por meio do Exchange Service. |
| `exchange-service-microsservice` | 8000 e 8001 | Consulta fatores de cambio e calcula valores convertidos. |
| `mysql` | 3306 | Persiste livros e taxas de cambio no schema compartilhado `microservice_section_7`. |
| `zipkin` | 9411 | Recebe e apresenta os traces distribuidos dos servicos ativos. |

O Config Server, Greeting Service, PostgreSQL, RabbitMQ e o fluxo User/Email permanecem no monorepo, mas estao comentados no `docker-compose.yml` principal e nao sobem no comando padrao.

## Gateway e Rotas

O Gateway usa rotas declarativas e URIs `lb://` resolvidas pelo Eureka:

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

As respostas incluem as portas das instancias envolvidas, permitindo observar o balanceamento de carga entre os containers.

## Swagger e CORS

O Gateway agrega os documentos OpenAPI do Book e do Exchange Service. As URLs de servidor sao relativas aos prefixos do Gateway, evitando que o Swagger exponha hostnames internos do Docker.

- Swagger UI: [localhost:8765/swagger-ui.html](http://localhost:8765/swagger-ui.html)
- Book OpenAPI: [localhost:8765/book-service-microsservice/v3/api-docs](http://localhost:8765/book-service-microsservice/v3/api-docs)
- Exchange OpenAPI: [localhost:8765/exchange-service-microsservice/v3/api-docs](http://localhost:8765/exchange-service-microsservice/v3/api-docs)

O CORS global do Gateway permite todas as origens, headers e os metodos `GET`, `POST`, `PUT`, `PATCH`, `DELETE` e `OPTIONS`. Essa politica e adequada ao laboratorio local e deve ser restringida antes de uma exposicao publica.

## Observabilidade com Zipkin

Gateway, Naming Server, Book e Exchange usam `spring-boot-starter-zipkin`, sampling de `100%` no ambiente de estudo e exportacao para `/api/v2/spans`.

- Interface: [localhost:9411](http://localhost:9411)
- Servicos esperados: `api-gateway`, `naming-server`, `book-service-microsservice` e `exchange-service-microsservice`.
- Logs das aplicacoes incluem `traceId` e `spanId` para correlacao com os spans do Zipkin.

Dentro do Docker, o endpoint e configurado como `http://zipkin:9411/api/v2/spans`. Na execucao local, o fallback usa `http://localhost:9411/api/v2/spans`.

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
    naming-server/                     # Eureka Server
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

Pre-requisitos: Docker com Compose e portas `3306`, `8000`, `8001`, `8100`, `8101`, `8671`, `8765` e `9411` livres.

```bash
docker compose up -d --build
```

Verifique os containers:

```bash
docker compose ps
```

Interfaces principais:

- Eureka: [localhost:8671](http://localhost:8671)
- API Gateway: [localhost:8765](http://localhost:8765)
- Swagger UI: [localhost:8765/swagger-ui.html](http://localhost:8765/swagger-ui.html)
- Zipkin: [localhost:9411](http://localhost:9411)

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
- O sampling de traces em `1.0` e o CORS aberto foram escolhidos para facilitar testes locais.
- Credenciais reais devem permanecer em variaveis de ambiente ou arquivos `.env` locais nao versionados.
