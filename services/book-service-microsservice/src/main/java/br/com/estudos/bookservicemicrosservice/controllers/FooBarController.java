package br.com.estudos.bookservicemicrosservice.controllers;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/book-service")
@Tag(name = "Foo Bar API", description = "API for demonstrating Resilience4j features")
public class FooBarController {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(FooBarController.class);

    @Operation(summary = "Demonstrates Resilience4j features like Retry, Circuit Breaker, Rate Limiter, and Bulkhead")
    @GetMapping("/foo-bar")
//    @Retry(name = "foo-bar")
//    @Retry(name = "foo-bar", fallbackMethod = "fallbackFooBar")
//    @CircuitBreaker(name = "foo-bar", fallbackMethod = "fallbackFooBar")
//    @RateLimiter(name= "foo-bar")
    @Bulkhead(name = "foo-bar")
    public String fooBar() {
        logger.info("Request to foo-bar endpoint received");

        // Simulating a call to another service using RestTemplate
        var response = new RestTemplate().getForEntity(
                "http://localhost:8080/foo-bar",
                String.class
        );
        logger.info("Response from foo-bar service: {}", response.getBody());
        return response.getBody();
        //return "Foo Bar";
    }

//    public String fallbackFooBar(Exception ex) {
//        logger.info("Fallback method called for foo-bar endpoint");
//        logger.error("Fallback method called for foo-bar endpoint", ex);
//        return "Fallback response for Foo Bar";
//    }
}
