package io.github.dongjulim.domain.stock.entity;

import io.github.dongjulim.domain.common.entity.BaseEntity;
import io.github.dongjulim.domain.common.exception.OutOfStockException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "stock")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "stock_seq", allocationSize = 1)
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_seq")
    private Long id;

    @Column(name = "product_id", nullable = false, unique = true)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Builder
    public Stock(
            Long id,
            Long productId,
            Integer quantity,
            LocalDateTime createAt,
            String createBy,
            LocalDateTime updateAt,
            String updateBy
    ) {
        super(createAt, createBy, updateAt, updateBy);
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    public void decrease(int amount) {
        if (this.quantity < amount) {
            throw new OutOfStockException();
        }
        this.quantity -= amount;
    }

    public void increase(int amount) {
        this.quantity += amount;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}
