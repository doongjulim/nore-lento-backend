package io.github.dongjulim.domain.stock.usecase.service;

import io.github.dongjulim.domain.common.exception.StockNotFoundException;
import io.github.dongjulim.domain.stock.dto.FindStockResponse;
import io.github.dongjulim.domain.stock.entity.Stock;
import io.github.dongjulim.domain.stock.repository.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindStockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private FindStockService findStockService;

    @Test
    @DisplayName("findStock - 상품 ID로 재고 정보를 조회한다")
    void findStock_shouldReturnStockInfo() {
        Stock stock = Stock.builder().id(1L).productId(10L).quantity(50).build();
        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));

        FindStockResponse response = findStockService.findStock(10L);

        assertThat(response.getProductId()).isEqualTo(10L);
        assertThat(response.getQuantity()).isEqualTo(50);
    }

    @Test
    @DisplayName("findStock - 재고 정보가 없으면 StockNotFoundException을 던진다")
    void findStock_throwsStockNotFoundException_whenStockNotFound() {
        given(stockRepository.findByProductId(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> findStockService.findStock(99L))
                .isInstanceOf(StockNotFoundException.class);
    }
}
