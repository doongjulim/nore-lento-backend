package io.github.dongjulim.domain.user.usecase;

import io.github.dongjulim.domain.user.dto.UpdateUserRoleRequest;

public interface UpdateUserRoleUseCase {
    void updateUserRole(Long userId, UpdateUserRoleRequest request);
}
