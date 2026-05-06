package io.github.dongjulim.api.controller.cart;

import io.github.dongjulim.domain.cart.dto.FindCartResponse;
import io.github.dongjulim.domain.cart.usecase.FindCartUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindCartController {

    private final FindCartUseCase findCartUseCase;

    @GetMapping("/api/v2/cart")
    public ResponseEntity<FindCartResponse> findCart(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        FindCartResponse response = findCartUseCase.findCart(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
