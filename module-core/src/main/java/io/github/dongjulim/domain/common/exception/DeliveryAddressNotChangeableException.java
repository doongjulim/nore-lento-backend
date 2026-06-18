package io.github.dongjulim.domain.common.exception;

public class DeliveryAddressNotChangeableException extends DomainException {

    public DeliveryAddressNotChangeableException() {
        super(ErrorCode.DELIVERY_ADDRESS_NOT_CHANGEABLE, "배송 준비 중일 때만 배송지를 변경할 수 있습니다.");
    }
}
