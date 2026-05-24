package io.github.dongjulim.api.controller.shippingAddress;

import io.github.dongjulim.domain.shippingAddress.usecase.DeleteShippingAddressUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteShippingAddressController {

    private final DeleteShippingAddressUseCase deleteShippingAddressUseCase;

    @DeleteMapping("/api/v2/shipping-addresses/{id}")
    public ResponseEntity<Void> deleteShippingAddress(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        deleteShippingAddressUseCase.deleteShippingAddress(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
