package ir.snapppay.paymentgateway.service.purchase;

import ir.snapppay.paymentgateway.controller.purchase.VerificationResultDto;
import ir.snapppay.paymentgateway.models.tables.records.PurchaseRecord;

public interface PurchaseService {
    PurchaseCreationResult create(PurchaseRecord purchase);

    VerificationResultDto.Status verifyOnPsp(Long purchaseId);
}
