package io.github.dongjulim.domain.common.exception;

public class CouponNotFoundException extends DomainException {

    public CouponNotFoundException() {
        super(ErrorCode.COUPON_NOT_FOUND, "쿠폰을 찾을 수 없습니다.");
    }
}
