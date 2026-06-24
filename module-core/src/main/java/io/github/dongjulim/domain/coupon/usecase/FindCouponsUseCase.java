package io.github.dongjulim.domain.coupon.usecase;

import io.github.dongjulim.domain.coupon.dto.FindCouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindCouponsUseCase {
    Page<FindCouponResponse> findCoupons(Pageable pageable);
}
