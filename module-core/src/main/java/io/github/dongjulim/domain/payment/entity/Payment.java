package io.github.dongjulim.domain.payment.entity;

import io.github.dongjulim.domain.common.entity.BaseEntity;
import io.github.dongjulim.domain.common.exception.PaymentNotRefundableException;
import io.github.dongjulim.domain.payment.enums.PaymentMethod;
import io.github.dongjulim.domain.payment.enums.PaymentStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "payment_seq", allocationSize = 1)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq")
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    private Long amount;

    @Column(name = "transaction_id")
    private String transactionId;

    @Builder
    public Payment(
            Long id,
            Long orderId,
            Long userId,
            PaymentMethod method,
            PaymentStatus status,
            Long amount,
            String transactionId,
            LocalDateTime createAt,
            String createBy,
            LocalDateTime updateAt,
            String updateBy
    ) {
        super(createAt, createBy, updateAt, updateBy);
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.method = method;
        this.status = status != null ? status : PaymentStatus.PENDING;
        this.amount = amount;
        this.transactionId = transactionId;
    }

    public void refund() {
        if (this.status != PaymentStatus.COMPLETED) {
            throw new PaymentNotRefundableException();
        }
        this.status = PaymentStatus.REFUNDED;
    }
}
