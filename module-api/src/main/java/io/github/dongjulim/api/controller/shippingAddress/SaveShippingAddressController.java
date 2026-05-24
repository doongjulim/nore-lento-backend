package io.github.dongjulim.api.controller.shippingAddress;

import io.github.dongjulim.domain.shippingAddress.dto.SaveShippingAddressRequest;
import io.github.dongjulim.domain.shippingAddress.usecase.SaveShippingAddressUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SaveShippingAddressController {

    private final SaveShippingAddressUseCase saveShippingAddressUseCase;

    @PostMapping("/api/v2/shipping-addresses")
    public ResponseEntity<Void> saveShippingAddress(
            @RequestBody @Valid SaveShippingAddressRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        saveShippingAddressUseCase.saveShippingAddress(request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
