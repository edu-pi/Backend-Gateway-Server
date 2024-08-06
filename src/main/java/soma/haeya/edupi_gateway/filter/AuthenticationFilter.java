package soma.haeya.edupi_gateway.filter;

import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import soma.haeya.edupi_gateway.auth.TokenProvider;
import soma.haeya.edupi_gateway.exception.UnAuthorizedException;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final TokenProvider jwtProvider;

    public AuthenticationFilter(TokenProvider jwtProvider) {
        super(Config.class);
        this.jwtProvider = jwtProvider;
    }

    @Setter
    @Getter
    public static class Config {

        private String requiredRole;

    }

    @Override
    public GatewayFilter apply(Config config) throws NullPointerException {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            MultiValueMap<String, HttpCookie> cookies = request.getCookies();

            String token = extractToken(cookies);

            if (!isJwtValid(token)) {
                throw new UnAuthorizedException(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.");
            }

            return chain.filter(exchange);
        });

    }

    // cookies에서 token을 추출하는 메서드
    public String extractToken(MultiValueMap<String, HttpCookie> cookies) {
        return Optional.ofNullable(cookies.getFirst("token"))
            .map(HttpCookie::getValue)
            .orElseThrow(() -> new UnAuthorizedException(HttpStatus.UNAUTHORIZED, "토큰이 없습니다."));
    }

    private boolean isJwtValid(String token) {
        try {
            jwtProvider.validateToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
