package io.github.dongjulim.api.controller.wishlist;

import io.github.dongjulim.domain.wishlist.dto.FindWishlistResponse;
import io.github.dongjulim.domain.wishlist.usecase.FindWishlistUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FindWishlistController {

    private final FindWishlistUseCase findWishlistUseCase;

    @GetMapping("/api/v2/wishlists")
    public ResponseEntity<List<FindWishlistResponse>> findWishlist(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(findWishlistUseCase.findWishlist(userDetails.getUsername()));
    }
}
