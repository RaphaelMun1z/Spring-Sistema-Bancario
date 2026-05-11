package sistema_bancario.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sistema_bancario.dtos.TokenDTO;
import sistema_bancario.entities.users.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {
    @Value("${api.security.token.secret:secret}")
    private String secret;

    private Algorithm algorithm;
    private static final String ISSUER = "sistema-bancario-api";

    @PostConstruct
    protected void init() {
        algorithm = Algorithm.HMAC256(secret);
    }

    public TokenDTO generateToken(User user) {
        try {
            Instant createdAt = Instant.now();
            Instant expiration = genExpirationDate();

            String token = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getEmail())
                    .withClaim("role", user.getRole())
                    .withIssuedAt(createdAt)
                    .withExpiresAt(expiration)
                    .sign(algorithm);

            return new TokenDTO(user.getUsername(), createdAt, expiration, token);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String validateToken(String token) {
        try {
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            System.out.println("Erro na validação do JWT: " + exception.getMessage());
            return null;
        }
    }

    private Instant genExpirationDate() {
        return Instant.now().plus(12, ChronoUnit.HOURS);
    }

}
