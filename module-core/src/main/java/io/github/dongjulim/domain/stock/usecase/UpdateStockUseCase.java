package io.github.dongjulim.domain.stock.usecase;

import io.github.dongjulim.domain.stock.dto.UpdateStockRequest;

public interface UpdateStockUseCase {

    void updateStock(Long productId, UpdateStockRequest request);
}
