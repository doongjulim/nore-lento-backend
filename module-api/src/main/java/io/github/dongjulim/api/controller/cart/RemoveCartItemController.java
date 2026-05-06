package io.github.dongjulim.api.controller.cart;

import io.github.dongjulim.domain.cart.usecase.RemoveCartItemUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RemoveCartItemController {

    private final RemoveCartItemUseCase removeCartItemUseCase;

    @DeleteMapping("/api/v2/cart/items")
    public ResponseEntity<Void> removeCartItems(
            @RequestParam List<Long> ids,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        removeCartItemUseCase.removeCartItems(ids, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
