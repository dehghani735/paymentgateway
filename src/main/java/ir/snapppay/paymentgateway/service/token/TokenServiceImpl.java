package ir.snapppay.paymentgateway.service.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

import static ir.snapppay.paymentgateway.models.Tables.CLIENT;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    /**
     * Used to fetch and save client.
     */
    private final DSLContext ctx;

    @Override
    public TokenDto generateToken(String apiKey, String secretKey, String userIp) {
        var client = getClientSecret(apiKey);
        var isValid = client != null && client.getSecretKey().equals(secretKey);
        if (!isValid) {
            throw new BadCredentialsException("api/secret keys not found");
        }

        if (!client.getIsActive())
            throw new AppException(CLIENT_NOT_ACTIVE);

        return getTokenDto(client.getCode());
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
}
