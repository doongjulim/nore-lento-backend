package io.github.dongjulim.domain.coupon.dto;

import io.github.dongjulim.domain.coupon.entity.Coupon;
import io.github.dongjulim.domain.coupon.entity.UserCoupon;
import io.github.dongjulim.domain.coupon.enums.DiscountType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindMyCouponResponse {

    private final Long id;
    private final Long couponId;
    private final String name;
    private final DiscountType discountType;
    private final Long discountValue;
    private final Long minOrderAmount;
    private final LocalDateTime expiresAt;
    private final Boolean isUsed;

    public static FindMyCouponResponse from(UserCoupon userCoupon, Coupon coupon) {
        return FindMyCouponResponse.builder()
                .id(userCoupon.getId())
                .couponId(coupon.getId())
                .name(coupon.getName())
                .discountType(coupon.getDiscountType())
                .discountValue(coupon.getDiscountValue())
                .minOrderAmount(coupon.getMinOrderAmount())
                .expiresAt(coupon.getExpiresAt())
                .isUsed(userCoupon.getIsUsed())
                .build();
    }
}
