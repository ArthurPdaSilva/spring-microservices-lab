//Caso eu queira descobrir as rotas do gateway, sem usar o application.yml, posso fazer via código, mas para isso preciso de um bean do tipo RouteLocator

//package br.com.estudos.apigateway.config;
//
//import org.springdoc.core.models.GroupedOpenApi;
//import org.springdoc.core.properties.SwaggerUiConfigParameters;
//import org.springdoc.core.properties.SwaggerUiConfigProperties;
//import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//public class OpenApiConfiguration {
//
//    @Bean
//    public SwaggerUiConfigParameters swaggerUiConfigParameters(SwaggerUiConfigProperties swaggerUiConfigProperties) {
//        return new SwaggerUiConfigParameters(swaggerUiConfigProperties);
//    }
//
//    @Bean
//    @Lazy(false)
//    public List<GroupedOpenApi> apis(SwaggerUiConfigParameters swaggerUiConfigParameters,
//                                     RouteDefinitionLocator routeDefinitionLocator) {
//        var routeDefinitions = routeDefinitionLocator.getRouteDefinitions().collectList().block();
//        var groups = new ArrayList<GroupedOpenApi>();
//
//        if(routeDefinitions != null) {
//          routeDefinitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> {
//              String name = routeDefinition.getId().replaceAll("-service", "");
//              swaggerUiConfigParameters.addGroup(name);
//              groups.add(GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build());
//          });
//        }
//
//        return groups;
//    }
//}
