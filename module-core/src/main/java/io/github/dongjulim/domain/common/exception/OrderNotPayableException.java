package io.github.dongjulim.domain.common.exception;

public class OrderNotPayableException extends DomainException {

    public OrderNotPayableException() {
        super(ErrorCode.ORDER_NOT_PAYABLE, "결제할 수 없는 주문입니다.");
    }
}
