package ir.snapppay.paymentgateway.controller;

import ir.snapppay.paymentgateway.service.purchase.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/purchases")
public class PurchaseController {

    /**
     * used to call purchase related services.
     */
    private final PurchaseService purchaseService;
}
