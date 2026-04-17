package io.github.dongjulim.domain.product.dto;

import io.github.dongjulim.domain.product.enums.ProductCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
public class FindProductRequest {

    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    private String keyword;
}
