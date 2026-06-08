package io.github.dongjulim.domain.common.exception;

public class InsufficientPointException extends DomainException {

    public InsufficientPointException() {
        super(ErrorCode.INSUFFICIENT_POINT, "포인트가 부족합니다.");
    }
}
