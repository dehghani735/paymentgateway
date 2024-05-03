package ir.snapppay.paymentgateway.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class PurchaseCreationResult {

    private final long purchaseId;

    private final String clientReferenceNumber;

    private final String pspSwitchingUrl;
}
