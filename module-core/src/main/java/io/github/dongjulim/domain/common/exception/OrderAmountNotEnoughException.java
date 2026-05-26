package io.github.dongjulim.domain.common.exception;

public class OrderAmountNotEnoughException extends DomainException {

    public OrderAmountNotEnoughException() {
        super(ErrorCode.ORDER_AMOUNT_NOT_ENOUGH, "최소 주문 금액을 충족하지 않습니다.");
    }
}
