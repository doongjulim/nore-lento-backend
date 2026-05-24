package io.github.dongjulim.api.controller.shippingAddress;

import io.github.dongjulim.domain.shippingAddress.usecase.SetDefaultShippingAddressUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SetDefaultShippingAddressController {

    private final SetDefaultShippingAddressUseCase setDefaultShippingAddressUseCase;

    @PatchMapping("/api/v2/shipping-addresses/{id}/default")
    public ResponseEntity<Void> setDefaultShippingAddress(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        setDefaultShippingAddressUseCase.setDefaultShippingAddress(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
