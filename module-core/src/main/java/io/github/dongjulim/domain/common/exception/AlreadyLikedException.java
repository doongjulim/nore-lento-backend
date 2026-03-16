package io.github.dongjulim.domain.common.exception;

public class AlreadyLikedException extends RuntimeException {

    public AlreadyLikedException() {
        super("이미 좋아요를 누른 공지사항입니다.");
    }
}
