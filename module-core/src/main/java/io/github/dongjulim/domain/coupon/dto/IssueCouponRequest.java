package io.github.dongjulim.domain.coupon.dto;

import io.github.dongjulim.domain.coupon.enums.DiscountType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class IssueCouponRequest {

    @NotBlank
    private String name;

    @NotNull
    private DiscountType discountType;

    @NotNull
    @Positive
    private Long discountValue;

    private Long minOrderAmount;

    private LocalDateTime expiresAt;
}
