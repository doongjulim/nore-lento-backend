package io.github.dongjulim.domain.common.exception;

public class OrderNotFoundException extends DomainException {

    public OrderNotFoundException() {
        super(ErrorCode.ORDER_NOT_FOUND, "존재하지 않는 주문입니다.");
    }
}
