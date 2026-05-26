package io.github.dongjulim.api.controller.wishlist;

import io.github.dongjulim.domain.wishlist.usecase.RemoveWishlistUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RemoveWishlistController {

    private final RemoveWishlistUseCase removeWishlistUseCase;

    @DeleteMapping("/api/v2/wishlists/{id}")
    public ResponseEntity<Void> removeWishlist(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        removeWishlistUseCase.removeWishlist(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
