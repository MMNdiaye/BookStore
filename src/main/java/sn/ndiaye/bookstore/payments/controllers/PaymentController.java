package sn.ndiaye.bookstore.payments.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sn.ndiaye.bookstore.payments.dtos.WebHookRequest;
import sn.ndiaye.bookstore.payments.services.PaymentService;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/payments")
public class PaymentController {
    private PaymentService paymentService;

    @PostMapping("/loan/webhook")
    public void handleWebHook(
            @RequestHeader Map<String, String> headers,
            @RequestBody String payload
    ) {
        paymentService.handleWebHookEvent(new WebHookRequest(headers, payload));
    }
}
