package io.github.dongjulim.domain.coupon.entity;

import io.github.dongjulim.domain.common.entity.BaseEntity;
import io.github.dongjulim.domain.coupon.enums.DiscountType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "coupon")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "coupon_seq", allocationSize = 1)
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coupon_seq")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

    @Column(name = "discount_value", nullable = false)
    private Long discountValue;

    @Column(name = "min_order_amount")
    private Long minOrderAmount;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Builder
    public Coupon(
            Long id,
            String name,
            DiscountType discountType,
            Long discountValue,
            Long minOrderAmount,
            LocalDateTime expiresAt,
            LocalDateTime createAt,
            String createBy,
            LocalDateTime updateAt,
            String updateBy
    ) {
        super(createAt, createBy, updateAt, updateBy);
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minOrderAmount = minOrderAmount;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isApplicable(long totalPrice) {
        return minOrderAmount == null || totalPrice >= minOrderAmount;
    }

    public long applyDiscount(long totalPrice) {
        if (discountType == DiscountType.FIXED) {
            return Math.max(0, totalPrice - discountValue);
        }
        return totalPrice * (100 - discountValue) / 100;
    }

    public void update(String name, Long discountValue, Long minOrderAmount, LocalDateTime expiresAt) {
        if (name != null) this.name = name;
        if (discountValue != null) this.discountValue = discountValue;
        if (minOrderAmount != null) this.minOrderAmount = minOrderAmount;
        if (expiresAt != null) this.expiresAt = expiresAt;
    }
}
