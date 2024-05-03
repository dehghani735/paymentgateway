package ir.snapppay.paymentgateway.service;

import ir.snapppay.paymentgateway.models.tables.records.PurchaseRecord;

public interface PurchaseService {
    PurchaseCreationResult create(PurchaseRecord purchase);
}
