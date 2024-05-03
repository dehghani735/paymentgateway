package ir.snapppay.paymentgateway.config.security;

import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

@Getter
@ToString
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * Represents the unauthorized JWT token.
     */
    private final String token;

    @Nullable
    private final String clientIp;

    public JwtAuthenticationToken(String token, @Nullable String clientIp) {
        super(List.of());
        this.token    = token;
        this.clientIp = clientIp;
    }

    /**
     * @return The token.
     */
    @Override
    public Object getCredentials() {
        return token;
    }

    @Nullable
    @Override
    public Object getPrincipal() {
        return null;
    }

    /**
     * A dummy username for the request.
     */
    @Override
    public String getName() {
        return "Not Authenticated Yet!";
    }

    /**
     * The request is not authenticated yet.
     */
    @Override
    public boolean isAuthenticated() {
        return false;
    }

    /**
     * @return A masked token used in logs.
     */
    public String getMaskedToken() {
        if (token.length() < 15)
            return token;

        return token.substring(0, 5) + "***" + token.substring(token.length() - 5);
    }
}
