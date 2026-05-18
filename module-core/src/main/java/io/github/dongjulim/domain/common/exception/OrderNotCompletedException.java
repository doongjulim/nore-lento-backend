package io.github.dongjulim.domain.common.exception;

public class OrderNotCompletedException extends DomainException {

    public OrderNotCompletedException() {
        super(ErrorCode.ORDER_NOT_COMPLETED, "결제가 완료된 주문이 아닙니다.");
    }
}
