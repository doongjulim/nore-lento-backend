package io.github.dongjulim.domain.product.usecase;

import io.github.dongjulim.domain.product.dto.UpdateProductRequest;

public interface UpdateProductUseCase {

    void updateProduct(Long id, UpdateProductRequest request);
}
