package io.github.dongjulim.api.controller.shippingAddress;

import io.github.dongjulim.domain.shippingAddress.dto.FindShippingAddressResponse;
import io.github.dongjulim.domain.shippingAddress.usecase.FindShippingAddressesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FindShippingAddressesController {

    private final FindShippingAddressesUseCase findShippingAddressesUseCase;

    @GetMapping("/api/v2/shipping-addresses")
    public ResponseEntity<List<FindShippingAddressResponse>> findShippingAddresses(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(findShippingAddressesUseCase.findShippingAddresses(userDetails.getUsername()));
    }
}
