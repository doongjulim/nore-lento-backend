package io.github.dongjulim.api.controller.order;

import io.github.dongjulim.domain.order.usecase.CancelOrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CancelOrderController {

    private final CancelOrderUseCase cancelOrderUseCase;

    @DeleteMapping("/api/v2/orders/{id}")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        cancelOrderUseCase.cancelOrder(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
