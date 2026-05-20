package io.github.dongjulim.domain.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class SaveProductRequest {

    @NotBlank
    private String name;

    @NotNull
    private Long price;

    private String description;

    @NotNull(message = "카테고리 ID는 필수입니다.")
    private Long categoryId;

    @NotNull(message = "초기 재고는 필수입니다.")
    @Min(0)
    private Integer stock;
}
