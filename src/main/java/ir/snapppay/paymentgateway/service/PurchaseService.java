package ir.snapppay.paymentgateway.service;

public interface PurchaseService {
    PurchaseCreationResult create(PurchaseRecord purchase);
}
