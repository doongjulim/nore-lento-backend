package io.github.dongjulim.domain.common.exception;

public class OrderNotCancellableException extends DomainException {

    public OrderNotCancellableException() {
        super(ErrorCode.ORDER_NOT_CANCELLABLE, "취소할 수 없는 주문입니다.");
    }
}
