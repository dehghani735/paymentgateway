package ir.snapppay.paymentgateway.controller.purchase;

import ir.snapppay.paymentgateway.service.purchase.PurchaseCreationResult;
import ir.snapppay.paymentgateway.service.purchase.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
