package ir.snapppay.paymentgateway.controller.token;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@Getter
@JsonAutoDetect(fieldVisibility = ANY)
public class GenerateTokenDto {

    /**
     * Represents the api key of client.
     * Example: {@code aYPFphbbJF}
     */
    @NotBlank(message = "apiKey.is_required")
    private String apiKey;

    /**
     * Represents the secret key of client.
     * Example: {@code yUQO1dAD4iJDnSEkzt9BuNN_LEZifpns_L27C8Jjm091XxbzNP}
     */
    @NotBlank(message = "secretKey.is_required")
    private String secretKey;
}
