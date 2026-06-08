package io.github.dongjulim.domain.stock.usecase.service;

import io.github.dongjulim.domain.common.exception.StockNotFoundException;
import io.github.dongjulim.domain.stock.dto.FindStockResponse;
import io.github.dongjulim.domain.stock.repository.StockRepository;
import io.github.dongjulim.domain.stock.usecase.FindStockUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindStockService implements FindStockUseCase {

    private final StockRepository stockRepository;

    @Override
    public FindStockResponse findStock(Long productId) {
        return stockRepository.findByProductId(productId)
                .map(FindStockResponse::from)
                .orElseThrow(StockNotFoundException::new);
    }
}
