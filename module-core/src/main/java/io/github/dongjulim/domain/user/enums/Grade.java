package io.github.dongjulim.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public enum Grade {

    NORMAL("일반", "GRADE_NORMAL"),
    VIP("VIP", "GRADE_VIP"),
    VVIP("VVIP", "GRADE_VVIP");

    private final String title;
    private final String value;

    public static Grade of(String value) {
        for (Grade grade : values()) {
            if (grade.value.equals(value)) {
                return grade;
            }
        }
        throw new IllegalArgumentException("잘못된 등급입니다.");
    }
}
