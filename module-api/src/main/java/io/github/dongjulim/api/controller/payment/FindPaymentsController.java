package io.github.dongjulim.api.controller.payment;

import io.github.dongjulim.domain.payment.dto.FindPaymentResponse;
import io.github.dongjulim.domain.payment.usecase.FindPaymentsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FindPaymentsController {

    private final FindPaymentsUseCase findPaymentsUseCase;

    @GetMapping("/api/v2/payments")
    public ResponseEntity<List<FindPaymentResponse>> findPayments(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<FindPaymentResponse> response = findPaymentsUseCase.findPayments(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
