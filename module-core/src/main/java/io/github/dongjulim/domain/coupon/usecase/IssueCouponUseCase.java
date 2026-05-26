package io.github.dongjulim.domain.coupon.usecase;

import io.github.dongjulim.domain.coupon.dto.IssueCouponRequest;

public interface IssueCouponUseCase {

    void issueCoupon(IssueCouponRequest request);
}
