package io.github.dongjulim.domain.user.dto;

import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.user.enums.Grade;
import io.github.dongjulim.domain.user.enums.Role;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindUserDetailResponse {

    private final Long id;
    private final String username;
    private final String name;
    private final Role role;
    private final Grade grade;
    private final LocalDateTime createAt;
    private final String createId;
    private final LocalDateTime updateAt;
    private final String updateId;

    public static FindUserDetailResponse from(User user) {
        return FindUserDetailResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .grade(user.getGrade())
                .createAt(user.getCreateAt())
                .createId(user.getCreateId())
                .updateAt(user.getUpdateAt())
                .updateId(user.getUpdateId())
                .build();
    }
}
