package io.github.dongjulim.domain.order.entity;

import io.github.dongjulim.domain.common.entity.BaseEntity;
import io.github.dongjulim.domain.common.exception.OrderNotCancellableException;
import io.github.dongjulim.domain.common.exception.OrderNotPayableException;
import io.github.dongjulim.domain.order.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "order_seq", allocationSize = 1)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private Long totalPrice;

    @Column(name = "shipping_address_id")
    private Long shippingAddressId;

    @Column(name = "used_points")
    private Long usedPoints;

    @Column(name = "user_coupon_id")
    private Long userCouponId;

    @Column(columnDefinition = "default 'false'")
    private Boolean deleteCheck;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Order(
            Long id,
            Long userId,
            Long shippingAddressId,
            OrderStatus status,
            Long totalPrice,
            Long usedPoints,
            Long userCouponId,
            Boolean deleteCheck,
            LocalDateTime createAt,
            String createBy,
            LocalDateTime updateAt,
            String updateBy
    ) {
        super(createAt, createBy, updateAt, updateBy);
        this.id = id;
        this.userId = userId;
        this.shippingAddressId = shippingAddressId;
        this.status = status != null ? status : OrderStatus.PENDING;
        this.totalPrice = totalPrice;
        this.usedPoints = usedPoints;
        this.userCouponId = userCouponId;
        this.deleteCheck = deleteCheck != null ? deleteCheck : false;
    }

    public void cancel() {
        if (this.status != OrderStatus.PENDING) {
            throw new OrderNotCancellableException();
        }
        this.status = OrderStatus.CANCELLED;
    }

    public void complete() {
        if (this.status != OrderStatus.PENDING) {
            throw new OrderNotPayableException();
        }
        this.status = OrderStatus.COMPLETED;
    }

    public void cancelByRefund() {
        if (this.status != OrderStatus.COMPLETED) {
            throw new OrderNotCancellableException();
        }
        this.status = OrderStatus.CANCELLED;
    }
}
