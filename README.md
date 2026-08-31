# Spring Microservices Lab

Monorepo de estudos de microsservicos com Spring Boot, Spring Cloud Config, OpenFeign, bancos relacionais, Flyway, Docker e mensageria com RabbitMQ.

Este repositorio agrupa projetos que antes estavam separados para facilitar consulta, execucao local e publicacao em um unico repositorio remoto: [github.com/ArthurPdaSilva/spring-microservices-lab](https://github.com/ArthurPdaSilva/spring-microservices-lab).

## Referencias

- Curso principal: [Microservices do 0 a GCP com Spring Boot, Kubernetes e Docker](https://www.udemy.com/course/microservices-do-0-a-gcp-com-spring-boot-kubernetes-e-docker/learn/lecture/51009249#reviews), usado como referencia para `spring-cloud-config-server`, `spring-config-server`, `greeting-service`, `book-service-microsservice` e `exchange-service-microsservice`.
- Referencia complementar: [Microsservicos com Spring Boot e RabbitMQ](https://www.youtube.com/watch?v=ZnECi2gatMs&t=6161s), usada como referencia para `user-email-service`.

## Estrutura

```text
spring-microservices-lab/
  config-repo/
    spring-config-server/              # Arquivos de configuracao consumidos pelo Spring Cloud Config Server
  docs/
    architecture.md                    # Visao dos servicos, fluxos e responsabilidades
    running-locally.md                 # Guia de execucao local
  services/
    spring-cloud-config-server/        # Servidor central de configuracao
    greeting-service/                  # Servico REST simples com propriedades externas
    exchange-service-microsservice/    # API de cambio com MySQL e Flyway
    book-service-microsservice/        # API de livros que consulta cambio via OpenFeign
    user-email-service/                # Microsservicos de usuario e email com RabbitMQ e PostgreSQL
  docker-compose.infra.yml             # Infraestrutura local compartilhada
```

## Servicos

| Servico | Porta padrao | Papel |
|---|---:|---|
| `spring-cloud-config-server` | 8888 | Expor configuracoes centralizadas do diretorio `config-repo/spring-config-server`. |
| `greeting-service` | 8080 | Consumir configuracoes externas e expor endpoints simples de saudacao. |
| `exchange-service-microsservice` | 8000 | Consultar taxas de cambio persistidas em MySQL, com migrations Flyway. |
| `book-service-microsservice` | 8100 | Buscar livros no MySQL e calcular preco convertido consultando o Exchange Service via OpenFeign. |
| `user` | variavel | Criar usuarios, persistir no PostgreSQL e publicar evento de email no RabbitMQ. |
| `email` | variavel | Consumir eventos RabbitMQ, enviar email e persistir status no PostgreSQL. |

## Principais conceitos praticados

- Configuracao centralizada com Spring Cloud Config Server.
- Externalizacao de propriedades por ambiente.
- Comunicacao HTTP entre microsservicos com OpenFeign.
- Persistencia com Spring Data JPA.
- Migrations versionadas com Flyway.
- Infraestrutura local com Docker Compose.
- Health checks e endpoints operacionais com Spring Actuator.
- Comunicacao assincrona com RabbitMQ no fluxo usuario-email.
- Separacao de responsabilidades entre servicos independentes.

## Execucao rapida

Suba a infraestrutura local quando precisar de MySQL, PostgreSQL ou RabbitMQ:

```bash
docker compose -f docker-compose.infra.yml up -d
```

Execute cada servico pelo wrapper Maven dentro da sua pasta:

```bash
cd services/spring-cloud-config-server
./mvnw spring-boot:run
```

O guia detalhado esta em [`docs/running-locally.md`](docs/running-locally.md).

## Config Server

O Config Server usa o perfil `native` por padrao para ler o diretorio local `config-repo/spring-config-server`:

```yaml
SPRING_PROFILES_ACTIVE=native
CONFIG_NATIVE_SEARCH_LOCATIONS=file:../../config-repo/spring-config-server
```

Quando quiser usar o backend Git do Spring Cloud Config contra este repositorio remoto, aponte `CONFIG_REPO_URI` para a URL SSH e use `CONFIG_REPO_LABEL=main`:

```bash
SPRING_PROFILES_ACTIVE=default CONFIG_REPO_URI=git@github.com:ArthurPdaSilva/spring-microservices-lab.git CONFIG_REPO_LABEL=main ./mvnw spring-boot:run
```

O `search-paths` padrao para o modo Git aponta para `config-repo/spring-config-server`, onde ficam os arquivos de configuracao dentro deste monorepo.

## Observacoes

- Os projetos originais foram agrupados sem `target/`, `.idea/`, `.git` e `.env`.
- Os servicos ainda mantem seus `pom.xml` independentes; este monorepo e uma organizacao de estudos, nao um build Maven multi-module.
- Credenciais reais devem ficar somente em variaveis de ambiente ou arquivos `.env` locais nao versionados.
