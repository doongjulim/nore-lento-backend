package io.github.dongjulim.domain.common.exception;

public class NoticeLikeNotFoundException extends RuntimeException {

    public NoticeLikeNotFoundException() {
        super("좋아요 정보를 찾을 수 없습니다.");
    }
}
