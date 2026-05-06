package io.github.dongjulim.domain.common.exception;

public class AlreadyLikedException extends DomainException {

    public AlreadyLikedException() {
        super(ErrorCode.ALREADY_LIKED, "이미 좋아요를 누른 공지사항입니다.");
    }
}
