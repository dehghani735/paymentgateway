package ir.snapppay.paymentgateway.controller.purchase;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import ir.snapppay.paymentgateway.models.tables.records.PurchaseRecord;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@Getter
@ToString(callSuper = true)
@JsonAutoDetect(fieldVisibility = ANY)
public class CreatePurchaseDto extends BasePurchaseDto {

    /**
     * Represents a callback which we should call in order to notify the client about a particular purchase state.
     * For example, when the user pays the purchase, we would call this callback and let the client know about
     * the payment. This must be a valid URL address. Maximum length is 1024 characters.
     * Example: {@code https://api.client.ir/purchases/123563/callback}
     */
    @NotBlank(message = "callbackUrl.is_required")
    @URL(message = "callbackUrl.is_invalid")
    @Length(max = 1024, message = "callbackUrl.max_length")
    private String callbackUrl;

    /**
     * Represents the user identifier in the client system.
     * Maximum length is 50 characters.
     * Example: {@code a.pourtaghi} or {@code 09185674534}
     */
    @Length(max = 50, message = "userIdentifier.max_length")
    private String userIdentifier;

    public PurchaseRecord toRecord() {
        return new PurchaseRecord()
                .setAmount(amount)
                .setWage(wage)
                .setCurrency(currency)
                .setCallbackUrl(callbackUrl)
                .setClientReferenceNumber(clientReferenceNumber)
                .setUserIdentifier(userIdentifier)
                .setPayerMobileNumber(payerMobileNumber != null ? payerMobileNumber.trim() : null)
                .setPayerCardNumber(payerCardNumber)
                .setPayerNationalCode(payerNationalCode)
                .setDescription(description)
                .setAdditionalData(additionalData == null ? "{}" : additionalData.toString());
    }
}
