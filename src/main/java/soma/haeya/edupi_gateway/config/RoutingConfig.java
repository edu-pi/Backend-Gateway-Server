package soma.haeya.edupi_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import soma.haeya.edupi_gateway.filter.AuthenticationFilter;

@Configuration
public class RoutingConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder, AuthenticationFilter authorizationFilter) {
        return builder.routes()

            .route(predicate -> predicate.path("/edupi_visualize/**")
                .filters(f -> f.filter(authorizationFilter.apply(config -> config.setRequiredRole("ROLE_USER"))))
                .uri("http://localhost:8081/")) // code_visualize

            .route(predicate -> predicate.path("/edupi_user/**")
                .uri("http://localhost:8083/")) // edupi_user

            .route(predicate -> predicate.path("/edupi_db/**")
                .filters(f -> f.filter(authorizationFilter.apply(config -> config.setRequiredRole("ROLE_USER"))))
                .uri("http://localhost:8084/")) // edupi_db
            .build();
    }

}
