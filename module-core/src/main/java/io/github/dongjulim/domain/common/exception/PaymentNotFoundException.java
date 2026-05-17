package io.github.dongjulim.domain.common.exception;

public class PaymentNotFoundException extends DomainException {

    public PaymentNotFoundException() {
        super(ErrorCode.PAYMENT_NOT_FOUND, "존재하지 않는 결제입니다.");
    }
}

