package io.github.dongjulim.domain.product.usecase;

import io.github.dongjulim.domain.product.dto.SaveProductRequest;

public interface SaveProductUseCase {

    void saveProduct(SaveProductRequest request, String username);
}
