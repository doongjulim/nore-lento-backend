package io.github.dongjulim.domain.common.exception;

public class NoticeNotFoundException extends RuntimeException {

    public NoticeNotFoundException() {
        super("존재하지 않는 공지사항입니다.");
    }

    public NoticeNotFoundException(String message) {
        super(message);
    }
}
