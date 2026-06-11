package io.github.dongjulim.domain.coupon.usecase;

import io.github.dongjulim.domain.coupon.dto.UpdateCouponRequest;

public interface UpdateCouponUseCase {
    void updateCoupon(Long couponId, UpdateCouponRequest request);
}
