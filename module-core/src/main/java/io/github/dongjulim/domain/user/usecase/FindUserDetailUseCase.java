package io.github.dongjulim.domain.user.usecase;

import io.github.dongjulim.domain.user.dto.FindUserDetailResponse;

public interface FindUserDetailUseCase {

    FindUserDetailResponse findUserDetail(Long id);
}
