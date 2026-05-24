package io.github.dongjulim.api.controller.shippingAddress;

import io.github.dongjulim.domain.shippingAddress.dto.UpdateShippingAddressRequest;
import io.github.dongjulim.domain.shippingAddress.usecase.UpdateShippingAddressUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UpdateShippingAddressController {

    private final UpdateShippingAddressUseCase updateShippingAddressUseCase;

    @PutMapping("/api/v2/shipping-addresses/{id}")
    public ResponseEntity<Void> updateShippingAddress(
            @PathVariable Long id,
            @RequestBody @Valid UpdateShippingAddressRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        updateShippingAddressUseCase.updateShippingAddress(id, request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
