package io.github.dongjulim.domain.common.exception;

public class InvalidPasswordException extends DomainException {

    public InvalidPasswordException() {
        super(ErrorCode.INVALID_PASSWORD, "현재 비밀번호가 일치하지 않습니다.");
    }
}
