package io.github.dongjulim.api.controller.cart;

import io.github.dongjulim.domain.cart.dto.AddCartItemRequest;
import io.github.dongjulim.domain.cart.usecase.AddCartItemUseCase;
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
public class AddCartItemController {

    private final AddCartItemUseCase addCartItemUseCase;

    @PostMapping("/api/v2/cart/items")
    public ResponseEntity<Void> addCartItem(
            @RequestBody @Valid AddCartItemRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        addCartItemUseCase.addCartItem(request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
