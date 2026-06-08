package io.github.dongjulim.domain.point.usecase;

import io.github.dongjulim.domain.point.dto.FindPointResponse;

public interface FindPointUseCase {
    FindPointResponse findPoint(String username);
}
