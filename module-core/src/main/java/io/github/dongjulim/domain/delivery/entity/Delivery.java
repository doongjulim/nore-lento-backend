package io.github.dongjulim.domain.delivery.entity;

import io.github.dongjulim.domain.common.entity.BaseEntity;
import io.github.dongjulim.domain.common.exception.DeliveryNotReturnableException;
import io.github.dongjulim.domain.common.exception.DeliveryStatusNotAdvancableException;
import io.github.dongjulim.domain.delivery.enums.DeliveryStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "delivery")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "delivery_seq", allocationSize = 1)
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_seq")
    private Long id;

    @Column(name = "order_id", nullable = false, unique = true)
    private Long orderId;

    @Column(nullable = false)
    private String address;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    @Builder
    public Delivery(
            Long id,
            Long orderId,
            String address,
            String trackingNumber,
            DeliveryStatus status,
            LocalDateTime createAt,
            String createBy,
            LocalDateTime updateAt,
            String updateBy
    ) {
        super(createAt, createBy, updateAt, updateBy);
        this.id = id;
        this.orderId = orderId;
        this.address = address;
        this.trackingNumber = trackingNumber;
        this.status = status != null ? status : DeliveryStatus.PREPARING;
    }

    public void advance() {
        if (this.status == DeliveryStatus.PREPARING) {
            this.status = DeliveryStatus.SHIPPING;
        } else if (this.status == DeliveryStatus.SHIPPING) {
            this.status = DeliveryStatus.DELIVERED;
        } else {
            throw new DeliveryStatusNotAdvancableException();
        }
    }

    public void returnDelivery() {
        if (this.status != DeliveryStatus.DELIVERED) {
            throw new DeliveryNotReturnableException();
        }
        this.status = DeliveryStatus.RETURNED;
    }
}
