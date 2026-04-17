package io.github.dongjulim.domain.notice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public enum Category {

    NOTICE("공지사항", "CATEGORY_NOTICE"),
    QA("Q&A", "CATEGORY_QA"),
    FREE("자유게시판", "CATEGORY_MAINTENANCE");

    private final String title;
    private final String value;

    public static Category of(String value) {
        for (Category category : values()) {
            if (category.value.equals(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("잘못된 카테고리입니다.");
    }
}
