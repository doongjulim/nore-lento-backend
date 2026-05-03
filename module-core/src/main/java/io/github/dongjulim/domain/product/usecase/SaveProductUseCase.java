package io.github.dongjulim.domain.product.usecase;

import io.github.dongjulim.domain.product.dto.ProductRequest;

public interface SaveProductUseCase {

    void saveProduct(ProductRequest request, String username);
}
