package io.github.dongjulim.domain.product.dto;

import io.github.dongjulim.domain.product.enums.ProductCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FindProductRequest {

    private ProductCategory category;
    private String keyword;
}
