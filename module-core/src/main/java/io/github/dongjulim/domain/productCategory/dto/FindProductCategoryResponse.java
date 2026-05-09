package io.github.dongjulim.domain.productCategory.dto;

import io.github.dongjulim.domain.productCategory.entity.ProductCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindProductCategoryResponse {

    private final Long id;
    private final String name;

    public static FindProductCategoryResponse from(ProductCategory category) {
        return FindProductCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
