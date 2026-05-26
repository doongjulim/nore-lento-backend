package io.github.dongjulim.domain.wishlist.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class AddWishlistRequest {

    @NotNull
    private Long productId;
}
