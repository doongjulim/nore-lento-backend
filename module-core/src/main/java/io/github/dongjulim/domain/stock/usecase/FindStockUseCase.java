package io.github.dongjulim.domain.stock.usecase;

import io.github.dongjulim.domain.stock.dto.FindStockResponse;

public interface FindStockUseCase {
    FindStockResponse findStock(Long productId);
}
