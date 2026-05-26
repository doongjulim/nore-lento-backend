package io.github.dongjulim.domain.wishlist.dto;

import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.wishlist.entity.Wishlist;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindWishlistResponse {

    private final Long id;
    private final Long productId;
    private final String productName;
    private final Long productPrice;
    private final LocalDateTime createAt;

    public static FindWishlistResponse from(Wishlist wishlist, Product product) {
        return FindWishlistResponse.builder()
                .id(wishlist.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productPrice(product.getPrice())
                .createAt(wishlist.getCreateAt())
                .build();
    }
}
