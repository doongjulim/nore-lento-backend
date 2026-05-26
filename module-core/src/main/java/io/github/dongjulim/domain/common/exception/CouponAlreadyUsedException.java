package io.github.dongjulim.domain.common.exception;

public class CouponAlreadyUsedException extends DomainException {

    public CouponAlreadyUsedException() {
        super(ErrorCode.COUPON_ALREADY_USED, "이미 사용된 쿠폰입니다.");
    }
}
