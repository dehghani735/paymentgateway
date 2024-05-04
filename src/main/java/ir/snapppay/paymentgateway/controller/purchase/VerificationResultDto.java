package ir.snapppay.paymentgateway.controller.purchase;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = ANY)
public class VerificationResultDto {

    /**
     * It is the status of verification. Possible states are:
     * <p>
     * * `SUCCESSFUL`: The verification was successful.
     * * `FAILED`: The verification failed. The payment amount will return to the user bank account.
     * * `REVERSED`: Because of a fraud in payment from user, the payment reversed. The payment amount will return to the user bank account. See xref:purchase_flow[] step 10.
     * * `UNKNOWN`: The verification was unknown (typically because of a network failure). The client could retry the verification for this purchase before its expiration. The client also could inquiry the purchase later to identify the actual payment verification state.
     * * `ALREADY_VERIFIED`: The purchase already verified. For example a purchase with `SUCCESS` state is already verified.
     * * `NOT_VERIFIABLE`: The purchase is not prepare to verify and thus is not verifiable. Only purchases in `READY_TO_VERIFY` state can be verified.
     */
    private final Status status;

    /**
     * Payment verification response statuses.
     */
    public enum Status {

        /**
         * The verification was successful.
         */
        SUCCESSFUL,

        /**
         * The purchase already verified.
         */
        ALREADY_VERIFIED,

        /**
         * The purchase state is invalid and not verifiable.
         */
        NOT_VERIFIABLE,

        /**
         * The verification failed.
         */
        FAILED,

        /**
         * The payment reversed (because of the fraud on payment from user).
         */
        REVERSED,

        /**
         * The verification was unknown.
         */
        UNKNOWN
    }
}
