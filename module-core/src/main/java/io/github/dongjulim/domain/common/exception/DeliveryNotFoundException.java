package io.github.dongjulim.domain.common.exception;

public class DeliveryNotFoundException extends DomainException {

    public DeliveryNotFoundException() {
        super(ErrorCode.DELIVERY_NOT_FOUND, "존재하지 않는 배송입니다.");
    }
}
