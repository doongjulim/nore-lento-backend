package io.github.dongjulim.domain.productCategory.usecase;

import io.github.dongjulim.domain.productCategory.dto.UpdateProductCategoryRequest;

public interface UpdateProductCategoryUseCase {

    void updateProductCategory(Long id, UpdateProductCategoryRequest request);
}
