package io.github.dongjulim.domain.user.dto;

import io.github.dongjulim.domain.user.enums.Grade;
import io.github.dongjulim.domain.user.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class UpdateUserRequest {

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    private String name;

    @NotNull
    private Role role;

    @NotNull
    private Grade grade;
}
