package ir.snapppay.paymentgateway.service;

import ir.snapppay.paymentgateway.models.tables.records.PurchaseRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    @Override
    public PurchaseCreationResult create(PurchaseRecord purchase) {
        return null;
    }
}
