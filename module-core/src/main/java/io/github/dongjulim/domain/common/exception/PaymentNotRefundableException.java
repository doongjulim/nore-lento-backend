package io.github.dongjulim.domain.common.exception;

public class PaymentNotRefundableException extends DomainException {

    public PaymentNotRefundableException() {
        super(ErrorCode.PAYMENT_NOT_REFUNDABLE, "환불할 수 없는 결제입니다.");
    }
}
