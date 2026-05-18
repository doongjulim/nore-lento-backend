package io.github.dongjulim.domain.common.exception;

public class DeliveryStatusNotAdvancableException extends DomainException {

    public DeliveryStatusNotAdvancableException() {
        super(ErrorCode.DELIVERY_STATUS_NOT_ADVANCABLE, "더 이상 배송 상태를 진행할 수 없습니다.");
    }
}
