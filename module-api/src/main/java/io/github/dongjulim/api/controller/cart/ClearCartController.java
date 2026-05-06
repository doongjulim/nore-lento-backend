package io.github.dongjulim.api.controller.cart;

import io.github.dongjulim.domain.cart.usecase.ClearCartUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClearCartController {

    private final ClearCartUseCase clearCartUseCase;

    @DeleteMapping("/api/v2/cart")
    public ResponseEntity<Void> clearCart(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        clearCartUseCase.clearCart(userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
