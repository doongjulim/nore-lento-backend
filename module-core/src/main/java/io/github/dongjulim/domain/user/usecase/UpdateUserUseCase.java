package io.github.dongjulim.domain.user.usecase;

import io.github.dongjulim.domain.user.dto.UpdateUserRequest;

public interface UpdateUserUseCase {

    void updateUser(Long id, UpdateUserRequest request);
}
