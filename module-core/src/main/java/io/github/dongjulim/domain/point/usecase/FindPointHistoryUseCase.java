package io.github.dongjulim.domain.point.usecase;

import io.github.dongjulim.domain.point.dto.FindPointHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindPointHistoryUseCase {
    Page<FindPointHistoryResponse> findPointHistory(String username, Pageable pageable);
}
