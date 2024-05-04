package ir.snapppay.paymentgateway.controller.token;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@Getter
@Setter
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = ANY)
public class TokenDto {

    /**
     * Represents the new access token.
     */
    private String accessToken;

    /**
     * Represents the new refresh token.
     */
    private String refreshToken;
}
