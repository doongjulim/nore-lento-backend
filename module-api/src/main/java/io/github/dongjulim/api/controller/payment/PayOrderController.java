package io.github.dongjulim.api.controller.payment;

import io.github.dongjulim.domain.payment.dto.PayOrderRequest;
import io.github.dongjulim.domain.payment.usecase.PayOrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class PayOrderController {

    private final PayOrderUseCase payOrderUseCase;

    @PostMapping("/api/v2/payments")
    public ResponseEntity<Void> payOrder(
            @RequestBody @Valid PayOrderRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        payOrderUseCase.payOrder(request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
