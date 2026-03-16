package io.github.dongjulim.domain.user.usecase;

import io.github.dongjulim.domain.user.dto.FindUserResponse;

import java.util.List;

public interface FindUserUseCase {

    List<FindUserResponse> findUser();
}
