package io.github.dongjulim.domain.stock.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UpdateStockRequest {

    @NotNull(message = "재고 수량은 필수입니다.")
    @Min(0)
    private Integer quantity;
}
