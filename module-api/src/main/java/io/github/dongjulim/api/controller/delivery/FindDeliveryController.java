package io.github.dongjulim.api.controller.delivery;

import io.github.dongjulim.domain.delivery.dto.FindDeliveryResponse;
import io.github.dongjulim.domain.delivery.usecase.FindDeliveryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindDeliveryController {

    private final FindDeliveryUseCase findDeliveryUseCase;

    @GetMapping("/api/v2/deliveries/{orderId}")
    public ResponseEntity<FindDeliveryResponse> findDelivery(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        FindDeliveryResponse response = findDeliveryUseCase.findDelivery(orderId, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
