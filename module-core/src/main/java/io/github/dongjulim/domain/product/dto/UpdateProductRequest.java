package io.github.dongjulim.domain.product.dto;

import io.github.dongjulim.domain.product.enums.ProductCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UpdateProductRequest {

    @NotBlank
    private String name;

    @NotNull
    private Long price;

    private String description;

    @NotNull
    private ProductCategory category;
}
