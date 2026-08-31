package br.com.estudos.controllers;

import br.com.estudos.configs.GreetingConfiguration;
import br.com.estudos.models.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/greeting")
public class GreetingController {

    private static final Logger log = LoggerFactory.getLogger(GreetingController.class);
    @Autowired
    public GreetingConfiguration greetingConfiguration;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Greeting greet() {
        return new Greeting(1L, "Hello, Arthur!");
    }

    @GetMapping("/properties")
    @ResponseStatus(HttpStatus.OK)
    public Greeting properties(
            @RequestParam String greeting,
            @RequestParam String defaultValue
    ) {
        log.info(
                "greeting='{}', length={}, blank={}",
                greeting,
                greeting.length(),
                greeting.isBlank()
        );

        if (greeting.isBlank() || defaultValue.isBlank()) {
            log.info("Using properties from application.yaml");
            return new Greeting(1L, greetingConfiguration.greeting() + ", " + greetingConfiguration.defaultValue());
        }
        log.info("Using custom properties from request parameters");
        return new Greeting(1L, greeting + ", " + defaultValue);
    }
}
