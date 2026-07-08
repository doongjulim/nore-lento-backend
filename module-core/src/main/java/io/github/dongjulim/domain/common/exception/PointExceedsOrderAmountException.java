package io.github.dongjulim.domain.common.exception;

public class PointExceedsOrderAmountException extends DomainException {

    public PointExceedsOrderAmountException() {
        super(ErrorCode.POINT_EXCEEDS_ORDER_AMOUNT, "사용 포인트가 주문 금액을 초과할 수 없습니다.");
    }
}
