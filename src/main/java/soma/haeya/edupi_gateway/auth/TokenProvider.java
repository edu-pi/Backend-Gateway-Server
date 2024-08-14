package soma.haeya.edupi_gateway.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import soma.haeya.edupi_gateway.exception.UnAuthorizedException;

@Component
public class TokenProvider {

    private final SecretKey key;

    public TokenProvider(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public void validateToken(String token) {
        Claims claims = getClaims(token);

        if (claims.getExpiration().before(new Date())) {
            throw new UnAuthorizedException(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.");
        }
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        } catch (JwtException e) {
            throw new UnAuthorizedException(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.");
        }
    }

}
