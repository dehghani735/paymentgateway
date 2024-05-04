package ir.snapppay.paymentgateway.service.purchase;

import ir.snapppay.paymentgateway.controller.purchase.VerificationResultDto;
import ir.snapppay.paymentgateway.models.tables.records.PurchaseRecord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static ir.snapppay.paymentgateway.config.security.AuthenticationUtil.currentClient;
import static ir.snapppay.paymentgateway.controller.purchase.VerificationResultDto.Status.SUCCESSFUL;
import static ir.snapppay.paymentgateway.models.Tables.PURCHASE;
import static ir.snapppay.paymentgateway.models.enums.PurchaseState.CREATED;
import static ir.snapppay.paymentgateway.models.enums.PurchaseState.PSP_VERIFY_SUCCESS;

@Slf4j
@Service
@AllArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final DSLContext ctx;

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

        purchase.store();

        return new PurchaseCreationResult(purchase.getId(), purchase.getClientReferenceNumber(),
                "pspSwitchingUrl");
    }

    @Override
    public VerificationResultDto.Status verifyOnPsp(Long purchaseId) {
        var purchase = ctx.fetchOne(PURCHASE, PURCHASE.ID.eq(purchaseId));
        if (purchase == null)
            throw new RuntimeException("PURCHASE_NOT_FOUND");

        purchase.setState(PSP_VERIFY_SUCCESS);

        purchase.store();
        return SUCCESSFUL;
    }
}
