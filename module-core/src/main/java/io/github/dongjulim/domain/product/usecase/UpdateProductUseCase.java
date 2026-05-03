package io.github.dongjulim.domain.product.usecase;

import io.github.dongjulim.domain.product.dto.ProductRequest;

public interface UpdateProductUseCase {

    void updateProduct(Long id, ProductRequest request);
}
