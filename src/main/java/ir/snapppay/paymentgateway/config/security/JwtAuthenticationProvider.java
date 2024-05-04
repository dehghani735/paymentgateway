package ir.snapppay.paymentgateway.config.security;

import ir.snapppay.paymentgateway.service.client.ClientDetails;
import ir.snapppay.paymentgateway.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final TokenService tokenService;

    /**
     * Tries to verify the given token .
     * If the given token corresponds to a valid client, then the authentication request
     * would be marked as a successful one. Otherwise, the request remain un-authenticated.
     */
    @Override
    @Nullable
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var request = (JwtAuthenticationToken) authentication;
        log.trace("About to verify an authentication token: {}", request.getMaskedToken());

        var client = tokenService.verifyToken(request.getToken()).orElseThrow(TokenVerificationException::new);

        checkClient(request.getClientIp(), client);
        log.trace("Successfully verified the {}'s ({}) token from IP {}",
                client.getCode(), client.getName(), request.getClientIp());

        return new AuthenticatedClient(client);
    }

    /**
     * This implementation can only support the [{@link UsernamePasswordAuthenticationToken}] authentication requests.
     */
    @Override
    public boolean supports(Class<?> authentication) {return authentication == JwtAuthenticationToken.class;}

    /**
     * Checks the client request IP and the activation status of the client.
     *
     * @param clientIp The client request IP to check.
     * @throws AccessDeniedException If IP not matched.
     */
    private void checkClient(@Nullable String clientIp, ClientDetails client) {
        if (!client.getIsActive())
            throw new RuntimeException("CLIENT_NOT_ACTIVE");

        if (clientIp == null) {
            log.warn("Client {} ({}) IP was null!", client.getCode(), client.getName());
            return;
        }

        var mustCheckIp  = !client.getIpWhiteList().isEmpty();
        var ipNotMatched = client.getIpWhiteList().stream().noneMatch(clientIp::contains);
        if (mustCheckIp && ipNotMatched) {
            log.warn("Client {} ({}) request from IP {} is not trusted", client.getCode(), client.getName(), clientIp);
            throw new RuntimeException("IP_NOT_TRUSTED");
        }
    }
}
