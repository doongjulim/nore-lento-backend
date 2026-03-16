package io.github.dongjulim.domain.user.usecase;

import io.github.dongjulim.domain.user.dto.SaveUserRequest;

public interface SaveUserUseCase {

    void saveUser(SaveUserRequest request);
}
