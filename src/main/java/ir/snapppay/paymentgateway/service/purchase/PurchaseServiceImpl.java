package ir.snapppay.paymentgateway.service.purchase;

import ir.snapppay.paymentgateway.models.tables.records.PurchaseRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static ir.snapppay.paymentgateway.config.security.AuthenticationUtil.currentClient;
import static ir.snapppay.paymentgateway.models.enums.PurchaseState.CREATED;

@Slf4j
@Service
@AllArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    @Override
    public PurchaseCreationResult create(PurchaseRecord purchase) {
        var client = currentClient()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Client is not logged in."));
        var createdDate = Instant.now();
        var expirationDate = purchase.getExpirationDate() != null
                ? purchase.getExpirationDate()
                : createdDate.plusSeconds(2000);
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
