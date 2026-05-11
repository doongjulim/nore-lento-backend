package io.github.dongjulim.domain.review.entity;

import io.github.dongjulim.domain.common.entity.BaseEntity;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "review_seq", allocationSize = 1)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_seq")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "default 'false'")
    private Boolean deleteCheck;

    @Builder
    public Review(
            Long id,
            Long userId,
            Long productId,
            String content,
            Integer rating,
            Boolean deleteCheck,
            LocalDateTime createAt,
            String createBy,
            LocalDateTime updateAt,
            String updateBy
    ) {
        super(createAt, createBy, updateAt, updateBy);
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.content = content;
        this.rating = rating;
        this.deleteCheck = deleteCheck != null ? deleteCheck : false;
    }

    public void updateReview(String content, Integer rating) {
        this.content = content;
        this.rating = rating;
    }

    public void deleteReview() {
        this.deleteCheck = true;
    }
}
