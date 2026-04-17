package io.github.dongjulim.domain.product.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public enum ProductCategory {

    FOOD("식품", "CATEGORY_FOOD"),
    CLOTHING("의류", "CATEGORY_CLOTHING"),
    ELECTRONICS("전자기기", "CATEGORY_ELECTRONICS");

    private final String title;
    private final String value;

    public static ProductCategory of(String value) {
        for (ProductCategory category : values()) {
            if (category.value.equals(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("잘못된 카테고리입니다.");
    }
}
