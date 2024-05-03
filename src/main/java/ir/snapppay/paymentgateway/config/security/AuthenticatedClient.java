package ir.snapppay.paymentgateway.config.security;

import ir.snapppay.paymentgateway.service.client.ClientDetails;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Getter
public class AuthenticatedClient extends AbstractAuthenticationToken {

    /**
     * The actual user.
     */
    private final ClientDetails client;

    public AuthenticatedClient(ClientDetails client) {
        super(List.of(new SimpleGrantedAuthority("ROLE_" + client.getCode())));
        this.client = client;
    }

    @Override
    public Object getCredentials() {
        return "N/A";
    }

    @Override
    public Object getPrincipal() {
        return client.getCode();
    }

    /**
     * This token represents a successful authentication request.
     */
    @Override
    public boolean isAuthenticated() {
        return true;
    }
}