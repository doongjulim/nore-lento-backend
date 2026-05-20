package io.github.dongjulim.domain.stock.usecase.service;

import io.github.dongjulim.domain.common.exception.StockNotFoundException;
import io.github.dongjulim.domain.stock.dto.UpdateStockRequest;
import io.github.dongjulim.domain.stock.entity.Stock;
import io.github.dongjulim.domain.stock.repository.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UpdateStockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private UpdateStockService updateStockService;

    @Test
    @DisplayName("updateStock - 재고 수량이 정상적으로 변경된다")
    void updateStock_shouldUpdateQuantity() {
        Stock stock = Stock.builder().id(1L).productId(10L).quantity(50).build();
        UpdateStockRequest request = new UpdateStockRequest();
        ReflectionTestUtils.setField(request, "quantity", 200);

        given(stockRepository.findByProductId(10L)).willReturn(Optional.of(stock));

        updateStockService.updateStock(10L, request);

        assertThat(stock.getQuantity()).isEqualTo(200);
    }

    @Test
    @DisplayName("updateStock - 재고 정보가 없으면 StockNotFoundException을 던진다")
    void updateStock_throwsStockNotFoundException_whenStockNotFound() {
        UpdateStockRequest request = new UpdateStockRequest();
        ReflectionTestUtils.setField(request, "quantity", 100);

        given(stockRepository.findByProductId(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> updateStockService.updateStock(99L, request))
                .isInstanceOf(StockNotFoundException.class);
    }
}
