package io.github.dongjulim.domain.coupon.entity;

import io.github.dongjulim.domain.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "user_coupon")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "user_coupon_seq", allocationSize = 1)
public class UserCoupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_coupon_seq")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    @Column(name = "is_used", nullable = false)
    private Boolean isUsed;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Builder
    public UserCoupon(
            Long id,
            Long userId,
            Long couponId,
            Boolean isUsed,
            LocalDateTime usedAt,
            LocalDateTime createAt,
            String createBy,
            LocalDateTime updateAt,
            String updateBy
    ) {
        super(createAt, createBy, updateAt, updateBy);
        this.id = id;
        this.userId = userId;
        this.couponId = couponId;
        this.isUsed = isUsed != null ? isUsed : false;
        this.usedAt = usedAt;
    }

    public void use() {
        this.isUsed = true;
        this.usedAt = LocalDateTime.now();
    }

    public void unuse() {
        this.isUsed = false;
        this.usedAt = null;
    }
}
