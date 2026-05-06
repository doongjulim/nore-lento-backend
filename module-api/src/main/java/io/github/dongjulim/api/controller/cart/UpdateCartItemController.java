package io.github.dongjulim.api.controller.cart;

import io.github.dongjulim.domain.cart.dto.UpdateCartItemRequest;
import io.github.dongjulim.domain.cart.usecase.UpdateCartItemUseCase;
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
public class UpdateCartItemController {

    private final UpdateCartItemUseCase updateCartItemUseCase;

    @PutMapping("/api/v2/cart/items/{cartItemId}")
    public ResponseEntity<Void> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        updateCartItemUseCase.updateCartItem(cartItemId, request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
