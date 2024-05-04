package ir.snapppay.paymentgateway.controller.purchase;

import ir.snapppay.paymentgateway.service.purchase.PurchaseCreationResult;
import ir.snapppay.paymentgateway.service.purchase.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Provides APIs to manage purchase-related APIs.
 *
 * @author Mohammad Reza Dehghani
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/purchases")
public class PurchaseController {

    /**
     * used to call purchase related services.
     */
    private final PurchaseService purchaseService;

    @PostMapping
    public PurchaseCreationResult createPurchase(@RequestBody @Validated CreatePurchaseDto dto) {
        log.debug("About to create a purchase request: {}", dto);

        return purchaseService.create(dto.toRecord());
    }

    @PostMapping(value = "/{purchaseId:\\d+}/verify")
    public VerificationResultDto.Status verifyPurchase(@PathVariable Long purchaseId) {
        log.debug("Received purchase verification request; purchaseId: {}", purchaseId);

        return purchaseService.verifyOnPsp(purchaseId);
    }
}
