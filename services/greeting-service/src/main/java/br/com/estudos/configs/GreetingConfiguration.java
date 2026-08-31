package br.com.estudos.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@ConfigurationProperties("greeting-service")

//O @RefreshScope permite que as propriedades sejam atualizadas em tempo de execução, sem a necessidade de reiniciar a aplicação. Isso é útil quando você deseja alterar as configurações do serviço sem interromper o serviço em execução.
//Ele não funciona em records
@RefreshScope
public class GreetingConfiguration {
    private String greeting;
    private String defaultValue;

    public String greeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public String defaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}

//public record GreetingConfiguration(String greeting, String defaultValue) {
//}
