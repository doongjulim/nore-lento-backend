package io.github.dongjulim.domain.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FindProductRequest {

    private Long categoryId;
    private String keyword;
    private Long minPrice;
    private Long maxPrice;
}
