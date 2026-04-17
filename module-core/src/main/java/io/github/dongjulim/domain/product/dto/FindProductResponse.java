package io.github.dongjulim.domain.product.dto;

import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.enums.ProductCategory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindProductResponse {

    private final Long id;
    private final String name;
    private final Long price;
    private final ProductCategory category;
    private final String sellerName;
    private final LocalDateTime createAt;

    public static FindProductResponse from(Product product) {
        return FindProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getCategory())
                .sellerName(product.getUser().getName())
                .createAt(product.getCreateAt())
                .build();
    }
}
