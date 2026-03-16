package io.github.dongjulim.domain.user.dto;

import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.user.enums.Grade;
import io.github.dongjulim.domain.user.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindUserResponse {

    private final Long id;
    private final String username;
    private final String name;
    private final Role role;
    private final Grade grade;

    public static FindUserResponse from(User user) {
        return FindUserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .grade(user.getGrade())
                .build();
    }
}
