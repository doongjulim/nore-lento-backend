package io.github.dongjulim.domain.common.exception;

public class DeliveryNotReturnableException extends DomainException {

    public DeliveryNotReturnableException() {
        super(ErrorCode.DELIVERY_NOT_RETURNABLE, "반품할 수 없는 배송입니다.");
    }
}
