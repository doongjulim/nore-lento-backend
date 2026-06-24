package io.github.dongjulim.domain.coupon.dto;

import io.github.dongjulim.domain.coupon.entity.Coupon;
import io.github.dongjulim.domain.coupon.enums.DiscountType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindCouponResponse {

    private final Long id;
    private final String name;
    private final DiscountType discountType;
    private final Long discountValue;
    private final Long minOrderAmount;
    private final LocalDateTime expiresAt;

    public static FindCouponResponse from(Coupon coupon) {
        return FindCouponResponse.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .discountType(coupon.getDiscountType())
                .discountValue(coupon.getDiscountValue())
                .minOrderAmount(coupon.getMinOrderAmount())
                .expiresAt(coupon.getExpiresAt())
                .build();
    }
}
