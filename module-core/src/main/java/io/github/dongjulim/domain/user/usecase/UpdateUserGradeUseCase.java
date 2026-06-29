package io.github.dongjulim.domain.user.usecase;

import io.github.dongjulim.domain.user.dto.UpdateUserGradeRequest;

public interface UpdateUserGradeUseCase {
    void updateUserGrade(Long userId, UpdateUserGradeRequest request);
}
