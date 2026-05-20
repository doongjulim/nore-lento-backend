package io.github.dongjulim.domain.common.exception;

public class StockNotFoundException extends DomainException {

    public StockNotFoundException() {
        super(ErrorCode.STOCK_NOT_FOUND, "재고 정보를 찾을 수 없습니다.");
    }
}
