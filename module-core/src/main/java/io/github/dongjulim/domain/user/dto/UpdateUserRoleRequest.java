package io.github.dongjulim.domain.user.dto;

import io.github.dongjulim.domain.user.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UpdateUserRoleRequest {

    @NotNull
    private Role role;
}
