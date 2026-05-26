package io.github.dongjulim.api.controller.wishlist;

import io.github.dongjulim.domain.wishlist.dto.AddWishlistRequest;
import io.github.dongjulim.domain.wishlist.usecase.AddWishlistUseCase;
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
public class AddWishlistController {

    private final AddWishlistUseCase addWishlistUseCase;

    @PostMapping("/api/v2/wishlists")
    public ResponseEntity<Void> addWishlist(
            @RequestBody @Valid AddWishlistRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        addWishlistUseCase.addWishlist(request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
