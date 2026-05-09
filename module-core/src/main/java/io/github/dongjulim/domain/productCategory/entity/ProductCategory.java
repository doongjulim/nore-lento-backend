package io.github.dongjulim.domain.productCategory.entity;

import io.github.dongjulim.domain.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "product_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "product_category_seq", allocationSize = 1)
public class ProductCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_category_seq")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "default 'false'")
    private Boolean deleteCheck;

    @Builder
    public ProductCategory(
            Long id,
            String name,
            Boolean deleteCheck,
            LocalDateTime createAt,
            String createBy,
            LocalDateTime updateAt,
            String updateBy
    ) {
        super(createAt, createBy, updateAt, updateBy);
        this.id = id;
        this.name = name;
        this.deleteCheck = deleteCheck != null ? deleteCheck : false;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void delete() {
        this.deleteCheck = true;
    }
}
