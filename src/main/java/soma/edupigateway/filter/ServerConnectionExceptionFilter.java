package soma.edupigateway.filter;

import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ServerConnectionExceptionFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
            .onErrorResume(ConnectException.class, throwable -> handleConnectException(exchange));
    }

    private Mono<Void> handleConnectException(ServerWebExchange exchange) {
        // 응답 설정
        exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);

        String responseBody = "The backend server is unavailable.";

        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        DataBuffer buffer = dataBufferFactory.wrap(responseBody.getBytes(StandardCharsets.UTF_8));

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
