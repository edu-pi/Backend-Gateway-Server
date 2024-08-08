package soma.haeya.edupi_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import soma.haeya.edupi_gateway.filter.AuthenticationFilter;

@Configuration
public class RoutingConfig {

    @Value("${server-url.db.url}")
    private String DB_URL;
    @Value("${server-url.db.default-path}")
    private String DB_PATH;

    @Value("${server-url.user.url}")
    private String USER_URL;
    @Value("${server-url.user.default-path}")
    private String USER_PATH;

    @Value("${server-url.visualize.url}")
    private String VISUALIZE_URL;
    @Value("${server-url.visualize.default-path}")
    private String VISUALIZE_PATH;


    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder, AuthenticationFilter authorizationFilter) {
        return builder.routes()

            .route(predicate -> predicate.path(VISUALIZE_PATH)
                .filters(f -> f.filter(authorizationFilter.apply(config -> config.setRequiredRole("ROLE_USER"))))
                .uri(VISUALIZE_URL)) // code_visualize

            .route(predicate -> predicate.path(USER_PATH)
                .uri(USER_URL)) // edupi_user

            .route(predicate -> predicate.path(DB_PATH)
                .uri(DB_URL)) // edupi_db

            .build();
    }

}
