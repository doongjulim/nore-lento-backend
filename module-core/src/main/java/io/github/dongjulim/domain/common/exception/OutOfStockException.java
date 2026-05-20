package io.github.dongjulim.domain.common.exception;

public class OutOfStockException extends DomainException {

    public OutOfStockException() {
        super(ErrorCode.OUT_OF_STOCK, "재고가 부족합니다.");
    }
}
