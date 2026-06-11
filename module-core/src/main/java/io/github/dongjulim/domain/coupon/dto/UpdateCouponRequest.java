package io.github.dongjulim.domain.coupon.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UpdateCouponRequest {

    private String name;

    @Positive
    private Long discountValue;

    private Long minOrderAmount;

    private LocalDateTime expiresAt;
}
