package io.github.dongjulim.domain.common.exception;

public class NoticeLikeNotFoundException extends DomainException {

    public NoticeLikeNotFoundException() {
        super(ErrorCode.NOTICE_LIKE_NOT_FOUND, "좋아요 정보를 찾을 수 없습니다.");
    }
}
