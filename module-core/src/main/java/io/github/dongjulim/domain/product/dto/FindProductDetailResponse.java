package io.github.dongjulim.domain.product.dto;

import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.enums.ProductCategory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindProductDetailResponse {

    private final Long id;
    private final String name;
    private final Long price;
    private final String description;
    private final ProductCategory category;
    private final LocalDateTime createAt;
    private final String createBy;
    private final LocalDateTime updateAt;
    private final String updateBy;

    public static FindProductDetailResponse from(Product product) {
        return FindProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .category(product.getCategory())
                .createAt(product.getCreateAt())
                .createBy(product.getCreateBy())
                .updateAt(product.getUpdateAt())
                .updateBy(product.getUpdateBy())
                .build();
    }
}
