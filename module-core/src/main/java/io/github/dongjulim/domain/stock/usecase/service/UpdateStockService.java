package io.github.dongjulim.domain.stock.usecase.service;

import io.github.dongjulim.domain.common.exception.StockNotFoundException;
import io.github.dongjulim.domain.stock.dto.UpdateStockRequest;
import io.github.dongjulim.domain.stock.entity.Stock;
import io.github.dongjulim.domain.stock.repository.StockRepository;
import io.github.dongjulim.domain.stock.usecase.UpdateStockUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateStockService implements UpdateStockUseCase {

    private final StockRepository stockRepository;

    @Override
    public void updateStock(Long productId, UpdateStockRequest request) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(StockNotFoundException::new);
        stock.updateQuantity(request.getQuantity());
    }
}
