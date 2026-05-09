package io.github.dongjulim.domain.productCategory.usecase;

import io.github.dongjulim.domain.productCategory.dto.SaveProductCategoryRequest;

public interface SaveProductCategoryUseCase {

    void saveProductCategory(SaveProductCategoryRequest request);
}
