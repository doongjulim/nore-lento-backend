package io.github.dongjulim.domain.common.exception;

public class NoticeNotFoundException extends DomainException {

    public NoticeNotFoundException() {
        super(ErrorCode.NOTICE_NOT_FOUND, "존재하지 않는 공지사항입니다.");
    }
}
