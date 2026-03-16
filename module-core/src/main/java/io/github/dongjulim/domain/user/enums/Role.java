package io.github.dongjulim.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public enum Role {

    USER("일반 사용자", "ROLE_USER"),
    ADMIN("일반 관리자", "ROLE_ADMIN"),
    MASTER("마스터", "ROLE_MASTER");

    private final String title;
    private final String value;

    public static Role of(String value) {
        for (Role role : values()) {
            if (role.value.equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("잘못된 권한입니다.");
    }
}
