package io.github.dongjulim.api.controller.delivery;

import io.github.dongjulim.domain.delivery.usecase.ReturnDeliveryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReturnDeliveryController {

    private final ReturnDeliveryUseCase returnDeliveryUseCase;

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @DeleteMapping("/api/v2/deliveries/{id}/return")
    public ResponseEntity<Void> returnDelivery(@PathVariable Long id) {
        returnDeliveryUseCase.returnDelivery(id);
        return ResponseEntity.ok().build();
    }
}
