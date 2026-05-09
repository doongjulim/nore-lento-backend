package io.github.dongjulim.domain.productCategory.usecase;

import io.github.dongjulim.domain.productCategory.dto.FindProductCategoryResponse;

import java.util.List;

public interface FindProductCategoryUseCase {

    List<FindProductCategoryResponse> findProductCategories();
}
