package io.github.dongjulim.domain.common.exception;

public class OrderAlreadyHasDeliveryException extends DomainException {

    public OrderAlreadyHasDeliveryException() {
        super(ErrorCode.ORDER_ALREADY_HAS_DELIVERY, "이미 배송이 등록된 주문입니다.");
    }
}
