package io.github.dongjulim.domain.common.exception;

public class PaymentGatewayException extends DomainException {

    public PaymentGatewayException() {
        super(ErrorCode.PAYMENT_GATEWAY_ERROR, "결제 게이트웨이 오류가 발생했습니다.");
    }
}
