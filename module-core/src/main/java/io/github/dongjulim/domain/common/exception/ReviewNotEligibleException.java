package io.github.dongjulim.domain.common.exception;

public class ReviewNotEligibleException extends DomainException {

    public ReviewNotEligibleException() {
        super(ErrorCode.REVIEW_NOT_ELIGIBLE, "구매 완료한 상품에만 리뷰를 작성할 수 있습니다.");
    }
}
