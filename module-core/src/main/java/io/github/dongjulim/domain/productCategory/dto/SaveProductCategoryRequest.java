package io.github.dongjulim.domain.productCategory.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class SaveProductCategoryRequest {

    @NotBlank(message = "카테고리 이름은 필수입니다.")
    private String name;
}
