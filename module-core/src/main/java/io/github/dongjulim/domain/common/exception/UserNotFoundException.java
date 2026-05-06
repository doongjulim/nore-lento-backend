package io.github.dongjulim.domain.common.exception;

public class UserNotFoundException extends DomainException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND, "존재하지 않는 유저입니다.");
    }
}
