package io.github.dongjulim.api.controller.payment;

import io.github.dongjulim.domain.payment.dto.FindPaymentResponse;
import io.github.dongjulim.domain.payment.usecase.FindPaymentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindPaymentController {

    private final FindPaymentUseCase findPaymentUseCase;

    @GetMapping("/api/v2/payments/{id}")
    public ResponseEntity<FindPaymentResponse> findPayment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        FindPaymentResponse response = findPaymentUseCase.findPayment(id, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
