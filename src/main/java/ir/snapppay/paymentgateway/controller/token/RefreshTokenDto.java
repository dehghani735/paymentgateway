package ir.snapppay.paymentgateway.controller.token;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@Getter
@JsonAutoDetect(fieldVisibility = ANY)
public class RefreshTokenDto {

    /**
     * Represents the refresh token. Used to generate a new pair of access/refresh tokens for the client.
     */
    @NotBlank(message = "refreshToken.is_required")
    private String refreshToken;
}
