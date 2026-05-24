package io.github.dongjulim.domain.shippingAddress.entity;

import io.github.dongjulim.domain.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "shipping_address")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "shipping_address_seq", allocationSize = 1)
public class ShippingAddress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shipping_address_seq")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    @Builder
    public ShippingAddress(
            Long id,
            Long userId,
            String recipientName,
            String phone,
            String address,
            String detailAddress,
            String zipCode,
            Boolean isDefault,
            LocalDateTime createAt,
            String createBy,
            LocalDateTime updateAt,
            String updateBy
    ) {
        super(createAt, createBy, updateAt, updateBy);
        this.id = id;
        this.userId = userId;
        this.recipientName = recipientName;
        this.phone = phone;
        this.address = address;
        this.detailAddress = detailAddress;
        this.zipCode = zipCode;
        this.isDefault = isDefault != null ? isDefault : false;
    }

    public void update(String recipientName, String phone, String address, String detailAddress, String zipCode) {
        this.recipientName = recipientName;
        this.phone = phone;
        this.address = address;
        this.detailAddress = detailAddress;
        this.zipCode = zipCode;
    }

    public void setDefault() {
        this.isDefault = true;
    }

    public void unsetDefault() {
        this.isDefault = false;
    }
}
