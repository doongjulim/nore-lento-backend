package io.github.dongjulim.domain.common.exception;

public class ReviewNotFoundException extends DomainException {

    public ReviewNotFoundException() {
        super(ErrorCode.REVIEW_NOT_FOUND, "존재하지 않는 리뷰입니다.");
    }
}
