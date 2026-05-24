package io.github.dongjulim.domain.common.exception;

public class ShippingAddressNotFoundException extends DomainException {

    public ShippingAddressNotFoundException() {
        super(ErrorCode.SHIPPING_ADDRESS_NOT_FOUND, "배송지를 찾을 수 없습니다.");
    }
}
