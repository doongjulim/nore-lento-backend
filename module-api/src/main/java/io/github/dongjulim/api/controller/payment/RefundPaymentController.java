package io.github.dongjulim.api.controller.payment;

import io.github.dongjulim.domain.payment.usecase.RefundPaymentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefundPaymentController {

    private final RefundPaymentUseCase refundPaymentUseCase;

    @DeleteMapping("/api/v2/payments/{id}")
    public ResponseEntity<Void> refundPayment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        refundPaymentUseCase.refundPayment(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
