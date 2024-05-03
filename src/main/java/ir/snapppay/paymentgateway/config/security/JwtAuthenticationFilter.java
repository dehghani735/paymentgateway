package ir.snapppay.paymentgateway.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class JwtAuthenticationFilter extends HttpFilter {

    /**
     * The bearer token prefix!
     */
    private static final String bearer = "Bearer ";

    /**
     * First off, it Extracts the `Bearer` token from the `Authorization` header. If such token exists, then
     * populates the auth request in the {@link SecurityContextHolder}. Otherwise, simply resumes the process.
     */
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        var checkAuth = !request.getRequestURI().startsWith("/v3/tokens");
        if (checkAuth) extractJwtToken(request);
        chain.doFilter(request, response);
    }

    private void extractJwtToken(HttpServletRequest request) {
        var token = extractBearerTokenFromHeader(request);

        var tokenIsPresent = token.isPresent() && StringUtils.hasText(token.get());
        var clientIp       = request.getHeader("X-REAL-IP");
        if (tokenIsPresent) {
            var authentication = new JwtAuthenticationToken(token.get(), clientIp);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    /**
     * Extracts the `Bearer` token from the request header and returns it. Also,
     * returns `null` when either the header is missing or the header value does
     * not follow the `Bearer <token>` format.
     */
    private Optional<String> extractBearerTokenFromHeader(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION);
        if (authorization != null && authorization.startsWith(bearer))
            return Optional.of(authorization.substring(bearer.length()));

        return Optional.empty();
    }
}
