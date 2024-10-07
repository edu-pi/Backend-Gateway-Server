package soma.edupigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import soma.edupigateway.filter.AuthenticationFilter;

@Configuration
public class RoutingConfig {

    @Value("${server-url.syntax.url}")
    private String SYNTAX_URL;
    @Value("${server-url.syntax.default-path}")
    private String SYNTAX_PATH;

    @Value("${server-url.visualize.url}")
    private String VISUALIZE_URL;
    @Value("${server-url.visualize.default-path}")
    private String VISUALIZE_PATH;

    @Value("${server-url.db.url}")
    private String DB_URL;
    @Value("${server-url.db.default-path}")
    private String DB_PATH;

    @Value("${server-url.user.url}")
    private String USER_URL;
    @Value("${server-url.user.default-path}")
    private String USER_PATH;

//    @Value("${server-url.lms.url}")
//    private String LMS_URL;
//    @Value("${server-url.lms.default-path}")
//    private String LMS_PATH;


    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder, AuthenticationFilter authorizationFilter) {
        return builder.routes()
            .route("root_route", r -> r.path("/")
                .filters(f -> f.setStatus(200))
                .uri("no://op")) // health check

            .route(predicate -> predicate.path(SYNTAX_PATH)
                .uri(SYNTAX_URL)) // edupi-syntax

            .route(predicate -> predicate.path(VISUALIZE_PATH)
//                .filters(f -> f.filter(authorizationFilter.apply(config -> config.setRequiredRole("ROLE_USER"))))
                .uri(VISUALIZE_URL)) // edupi-visualize

//            .route(predicate -> predicate.path(LMS_PATH)
//                .filters(f -> f.filter(authorizationFilter.apply(config -> config.setRequiredRole("ROLE_USER"))))
//                .uri(LMS_URL)) // edupi-lms

            .route(predicate -> predicate.path(USER_PATH)
                .uri(USER_URL)) // edupi-user

            .route(predicate -> predicate.path(DB_PATH)
                .uri(DB_URL)) // edupi-db

            .build();
    }

}
