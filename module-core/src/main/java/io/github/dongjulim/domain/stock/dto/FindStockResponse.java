package io.github.dongjulim.domain.stock.dto;

import io.github.dongjulim.domain.stock.entity.Stock;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindStockResponse {

    private final Long productId;
    private final Integer quantity;

    public static FindStockResponse from(Stock stock) {
        return new FindStockResponse(stock.getProductId(), stock.getQuantity());
    }
}
