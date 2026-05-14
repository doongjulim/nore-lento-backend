package io.github.dongjulim.domain.common.exception;

public class NotificationNotFoundException extends DomainException {

    public NotificationNotFoundException() {
        super(ErrorCode.NOTIFICATION_NOT_FOUND, "존재하지 않는 알림입니다.");
    }
}
