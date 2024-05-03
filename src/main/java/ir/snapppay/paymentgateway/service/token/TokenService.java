package ir.snapppay.paymentgateway.service.token;

import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;

public interface TokenService {

    /**
     * Generates access and refresh token for the given apiKey.
     *
     * @param apiKey    Represents the client api key.
     * @param secretKey Represents the client secret key.
     * @param userIp    Represents the user ip.
     * @throws BadCredentialsException When provided api/secret keys are not valid or the client is disabled.
     */
    TokenDto generateToken(String apiKey, String secretKey, String userIp);

    /**
     * Refreshes access and refresh token for the given refresh token.
     *
     * @throws BadCredentialsException When provided refresh token is not valid or the client is disabled.
     */
    TokenDto refreshToken(String refreshToken);

    /**
     * Verifies the given access token.
     *
     * @return The client owning the given access token.
     */
    Optional<ClientDetails> verifyToken(String accessToken);
}
