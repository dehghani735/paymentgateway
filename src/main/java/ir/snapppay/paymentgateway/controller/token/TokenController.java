package ir.snapppay.paymentgateway.controller.token;

import ir.snapppay.paymentgateway.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/tokens")
public class TokenController {

    /**
     * Used to generate access/refresh tokens.
     */
    private final TokenService tokenService;

    /**
     * Generates a pair of access/refresh tokens by using API/secret keys.
     *
     * <p>
     * **Possible Error Codes:**
     * <p>
     * * `security.bad_credentials`: When provided api/secret keys are not valid.
     * * `client.not_active`: When the client is not active.
     * * `apiKey.is_required`: When api key is not provided.
     * * `secretKey.is_required`: When secret Key is not provided.
     * * `web.invalid_or_missing_body`: When the request body is not a valid JSON. xref:invalid_or_missing_body[Read More.]
     * * `server.error`: Internal server error. Please tell us the value of fingerprint to be able to track the exact problem internally.
     * <p>
     * `Authorization` header parameter is NOT required.
     *
     * @return The generated pair of access/refresh tokens.
     */
    @PostMapping
    public TokenDto generateToken(@RequestBody @Validated GenerateTokenDto dto,
                                  @RequestHeader(name = "X-REAL-IP") String userIp) {
        log.debug("About to generate a pair of access/refresh tokens for api key: {}", dto.getApiKey());
        return tokenService.generateToken(dto.getApiKey(), dto.getSecretKey(), userIp);
    }

    /**
     * Refreshes the access/refresh tokens by using previously generated refresh token.
     * <p>
     * **Possible Error Codes:**
     * <p>
     * * `security.bad_credentials`: When provided refresh token is not valid.
     * * `client.not_active`: When the client is not active.
     * * `refreshToken.is_required`: When refresh token is not provided.
     * * `web.invalid_or_missing_body`: When the request body is not a valid JSON. xref:invalid_or_missing_body[Read More.]
     * * `server.error`: Internal server error. Please tell us the value of fingerprint to be able to track the exact problem internally.
     * <p>
     * `Authorization` header parameter is NOT required.
     *
     * @return The generated pair of access/refresh tokens.
     */
    @PostMapping("/refresh")
    public TokenDto refreshToken(@RequestBody @Validated RefreshTokenDto dto) {
        log.debug("About to refresh tokens...");
        return tokenService.refreshToken(dto.getRefreshToken());
    }
}
