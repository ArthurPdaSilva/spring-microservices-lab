package br.com.estudos.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        //lb://book-service is the name of the service registered in Eureka
//        return builder.routes().route(r -> r.path("/book-service-microsservice/**").uri("lb://book-service")).route(
//                r -> r.path()
//        ).build();
//                .route(r -> r.path("/get/").uri("http://localhost:8080")).build();
//    }
}
