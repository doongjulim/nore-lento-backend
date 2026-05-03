package io.github.dongjulim.domain.product.entity;

import io.github.dongjulim.domain.common.entity.BaseEntity;
import io.github.dongjulim.domain.product.enums.ProductCategory;
import io.github.dongjulim.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "product_seq", allocationSize = 1)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(columnDefinition = "default 'false'")
    private Boolean deleteCheck;

    @Builder
    public Product(
            Long id,
            Long userId,
            String name,
            Long price,
            String description,
            ProductCategory category,
            Boolean deleteCheck,
            LocalDateTime createAt,
            String createBy,
            LocalDateTime updateAt,
            String updateBy
    ) {
        super(createAt, createBy, updateAt, updateBy);
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.deleteCheck = deleteCheck != null ? deleteCheck : false;
    }

    public void updateProduct(String name, Long price, String description, ProductCategory category) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    public void deleteProduct() {
        this.deleteCheck = true;
    }
}
