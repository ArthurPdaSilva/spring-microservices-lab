# Spring Config Repository

Repositorio de configuracoes usado pelo `spring-cloud-config-server` dentro do monorepo `spring-microservices-lab`.

## Conteudo

- `application.yml`: configuracao padrao do `greeting-service`.
- `greeting-service/application-*.yml`: configuracoes por perfil/idioma consumidas pelo Config Server.

## Uso

O Config Server aponta para este diretorio por padrao usando o perfil `native`:

```yaml
SPRING_PROFILES_ACTIVE=native
CONFIG_NATIVE_SEARCH_LOCATIONS=file:../../config-repo/spring-config-server
```

Se a configuracao for lida do repositorio remoto, use `SPRING_PROFILES_ACTIVE=default`, `CONFIG_REPO_URI=git@github.com:ArthurPdaSilva/spring-microservices-lab.git` e `CONFIG_REPO_LABEL=main` ao iniciar o Config Server.
