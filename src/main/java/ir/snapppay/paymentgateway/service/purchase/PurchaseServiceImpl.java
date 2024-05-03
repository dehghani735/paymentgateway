package ir.snapppay.paymentgateway.service.purchase;

import ir.snapppay.paymentgateway.models.tables.records.PurchaseRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static ir.snapppay.paymentgateway.models.enums.PurchaseState.CREATED;

@Slf4j
@Service
@AllArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    @Override
    public PurchaseCreationResult create(PurchaseRecord purchase) {

        purchase
                .setState(CREATED)
                .setClientCode(client.getCode())
                .setClientConfigId(client.getLatestConfigId())
                .setCreatedAt(createdDate)
                .setModifiedAt(createdDate)
                .setExpirationDate(expirationDate);

        return null;
    }
}
