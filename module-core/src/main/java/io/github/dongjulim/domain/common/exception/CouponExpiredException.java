package io.github.dongjulim.domain.common.exception;

public class CouponExpiredException extends DomainException {

    public CouponExpiredException() {
        super(ErrorCode.COUPON_EXPIRED, "만료된 쿠폰입니다.");
    }
}
