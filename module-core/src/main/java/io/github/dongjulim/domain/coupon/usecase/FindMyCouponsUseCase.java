package io.github.dongjulim.domain.coupon.usecase;

import io.github.dongjulim.domain.coupon.dto.FindMyCouponResponse;

import java.util.List;

public interface FindMyCouponsUseCase {

    List<FindMyCouponResponse> findMyCoupons(String username);
}
