package io.github.dongjulim.domain.product.usecase;

import io.github.dongjulim.domain.product.dto.FindProductDetailResponse;

public interface FindProductDetailUseCase {

    FindProductDetailResponse findProductDetail(Long id);
}
