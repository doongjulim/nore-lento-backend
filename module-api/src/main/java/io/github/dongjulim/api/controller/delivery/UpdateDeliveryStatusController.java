package io.github.dongjulim.api.controller.delivery;

import io.github.dongjulim.domain.delivery.usecase.UpdateDeliveryStatusUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateDeliveryStatusController {

    private final UpdateDeliveryStatusUseCase updateDeliveryStatusUseCase;

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @PatchMapping("/api/v2/deliveries/{id}/status")
    public ResponseEntity<Void> updateDeliveryStatus(@PathVariable Long id) {
        updateDeliveryStatusUseCase.updateDeliveryStatus(id);
        return ResponseEntity.ok().build();
    }
}
