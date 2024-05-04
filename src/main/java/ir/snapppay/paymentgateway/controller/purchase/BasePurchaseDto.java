package ir.snapppay.paymentgateway.controller.purchase;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ir.snapppay.paymentgateway.models.enums.Currency;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@Getter
@ToString
@JsonAutoDetect(fieldVisibility = ANY)
public class BasePurchaseDto {

    /**
     * Represents the phone number pattern.
     */
    public static final String PHONE_NUMBER_PATTERN = "\\s*(?:(?:\\+|00)98|0)9\\d{9}\\s*";

    /**
     * Represents the amount of purchase.
     * Example: {@code 250000}
     */
    @NotNull(message = "amount.is_required")
    @Min(value = 5000, message = "amount.not_enough")
    protected Long amount;

    /**
     * Represents the wage of purchase. The user will pay the `amount` + `wage`. default value is 0.
     * Example: {@code 5000}
     */
    @Min(value = 0, message = "wage.is_invalid")
    protected Long wage;

    /**
     * Represents the currency of {@link #amount} and {@link #wage}.
     */
    @NotNull(message = "currency.is_required")
    protected Currency currency;

    /**
     * Represents a reference number provided by client to trace purchase in our system.
     * The reference number between client purchases must be unique. We will check this on our side.
     * Maximum length is 50 characters.
     * Example: {@code 5435436}
     */
    @NotBlank(message = "clientReferenceNumber.is_required")
    @Length(max = 50, message = "clientReferenceNumber.max_length")
    protected String clientReferenceNumber;

    /**
     * Represents the mobile number of the payer. It will be sent to Psp to autocomplete the payment fields.
     * *Note:* If `checkPayerNationalCode` is true, this field is required.
     * Example: {@code 09131234321}
     */
    @Nullable
    @Pattern(regexp = PHONE_NUMBER_PATTERN, message = "payerMobileNumber.is_invalid")
    protected String payerMobileNumber;

    /**
     * Bank card number which is required to do the transaction.
     * Only this card number can be used to do the transaction. Optional.
     * Example: {@code 6037997122223333}
     */
    @Nullable
    protected String payerCardNumber;

    /**
     * Represents the national code of card owner. Used to verify the identity of card owner.
     * This will be applied only if the `checkPayerNationalCode` is true
     * and only required when the `checkPayerNationalCode` is true.
     * Example: {@code 0921456778}
     */
    @Nullable
    protected String payerNationalCode;

    /**
     * Used to check national code on PSP side.
     * If set true, client must also provide the `payerNationalCode` and `payerMobileNumber` values.
     * The default value is false.
     */
    @Nullable
    protected Boolean checkPayerNationalCode;

    /**
     * Represents the client provided description. Optional.
     * Maximum length is 256 characters.
     * Example: {@code some related description}
     */
    @Nullable
    @Length(max = 256, message = "description.max_length")
    protected String description;

    /**
     * Represents the additional data provided by client in JSON format. Optional.
     * Example: {@code {"someTag": "some-value"}}
     */
    @Nullable
    protected ObjectNode additionalData;

    @JsonIgnore
    @AssertTrue(message = "amount_plus_wage.max_value_exceeded")
    public boolean isAmountAndWageCorrect() {
        wage = wage != null ? wage : 0;
        return amount + wage <= 100_000_0000;
    }

    @JsonIgnore
    @AssertTrue(message = "payerNationalCode_and_payerMobileNumber.are_required")
    public boolean isNationalCodeAndMobileNumberPresent() {
        return checkPayerNationalCode == null || !checkPayerNationalCode ||
               payerNationalCode != null && payerMobileNumber != null;
    }
}
