package soma.haeya.edupi_gateway.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class UnAuthorizedException extends ResponseStatusException {

    public UnAuthorizedException(HttpStatus status, String message) {
        super(status, message);
    }
}
