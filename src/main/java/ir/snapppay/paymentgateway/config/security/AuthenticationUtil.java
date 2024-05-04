package ir.snapppay.paymentgateway.config.security;

import ir.snapppay.paymentgateway.service.token.ClientDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
public class AuthenticationUtil {

    /**
     * Returns the code of current authenticated client.
     * It fetches the client details from Spring Security context.
     *
     * @return The {@link ClientDetails} fetched from database on token verification step.
     * @throws AuthenticationCredentialsNotFoundException When user is not logged in.
     */
    public static String currentClientCode() throws AuthenticationCredentialsNotFoundException {
        return currentClient()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Client is not logged in."))
                .getCode();
    }

    /**
     * Returns the current authenticated client.
     * It fetches the client details from Spring Security context.
     *
     * @return The {@link Optional} of possibly found {@link ClientDetails}.
     */
    public static Optional<ClientDetails> currentClient() {
        var     authentication  = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.getClass().equals(AuthenticatedClient.class);

        return Optional.ofNullable(authentication)
                .filter(it -> isAuthenticated).map(it -> ((AuthenticatedClient) authentication).getClient());
    }
}
