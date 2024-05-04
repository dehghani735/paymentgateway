package ir.snapppay.paymentgateway.service.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import ir.snapppay.paymentgateway.controller.token.TokenDto;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static ir.snapppay.paymentgateway.models.Tables.CLIENT;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "tokens")
public class TokenServiceImpl implements TokenService {

    /**
     * Used to fetch and save client.
     */
    private final DSLContext ctx;

    /**
     * Represents the expiration duration of access token.
     */
    @Setter
    @NotNull(message = "Token expiration is required.")
    private Duration jwtExpireAfter;

    /**
     * Represents the issuer for JWT tokens.
     */
    @Setter
    @Getter
    @NotNull(message = "JWT issuer is required")
    private String jwtIssuer;

    /**
     * Represents the secret for signing the JWT tokens.
     */
    @Setter
    @Getter
    @NotNull(message = "JWT Token secret is required")
    private String secret;

    /**
     * Audience of JWT.
     */
    @Setter
    @Getter
    @NotNull(message = "JWT audience is required")
    private String jwtAudience;

    /**
     * used to sign the JWT tokens.
     */
    @Getter
    private Algorithm algorithm;

    /**
     * JWT verifier instance.
     */
    private JWTVerifier verifier;

    /**
     * Initializes the JWT token signer algorithm and verifier.
     */
    @PostConstruct
    private void initialize() {
        algorithm = Algorithm.HMAC512(secret);
        verifier  = JWT.require(algorithm).withIssuer(jwtIssuer).withAudience(jwtAudience).build();
    }

    @Override
    public TokenDto generateToken(String apiKey, String secretKey, String userIp) {
        var client = getClientSecret(apiKey);
        var isValid = client != null && client.getSecretKey().equals(secretKey);
        if (!isValid) {
            throw new BadCredentialsException("api/secret keys not found");
        }

        if (!client.getIsActive())
            throw new RuntimeException("CLIENT_NOT_ACTIVE");

        return getTokenDto(client.getCode());
    }

    /**
     * Generates a token pair for the given client code.
     */
    private TokenDto getTokenDto(String clientCode) {
        var accessExpirationDate  = Date.from(Instant.now().plus(jwtExpireAfter));
        var refreshExpirationDate = Date.from(Instant.now().plus(jwtExpireAfter).plus(jwtExpireAfter));

        var accessToken  = generateToken(clientCode, accessExpirationDate, "access");
        var refreshToken = generateToken(clientCode, refreshExpirationDate, "refresh");

        return new TokenDto(accessToken, refreshToken);
    }

    @Nullable
    private ClientSecret getClientSecret(String apiKey) {
        return ctx.select(CLIENT.CODE, CLIENT.SECRET_KEY, CLIENT.IS_ACTIVE)
                .from(CLIENT)
                .where(CLIENT.API_KEY.eq(apiKey))
                .fetchOne(ClientSecret.MAPPER);
    }

    @Override
    public TokenDto refreshToken(String refreshToken) {
        return null;
    }

    @Override
    public Optional<ClientDetails> verifyToken(String accessToken) {
        return Optional.empty();
    }

    /**
     * Generate access/refresh token by provided subject and expiration date.
     */
    private String generateToken(String subject, Date expirationDate, String type) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(expirationDate)
                .withIssuedAt(Date.from(Instant.now()))
                .withIssuer(jwtIssuer)
                .withAudience(jwtAudience)
                .withClaim(type, true)
                .sign(algorithm);
    }
}
