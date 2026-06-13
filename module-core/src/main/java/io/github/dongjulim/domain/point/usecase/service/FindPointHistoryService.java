package io.github.dongjulim.domain.point.usecase.service;

import io.github.dongjulim.domain.point.dto.FindPointHistoryResponse;
import io.github.dongjulim.domain.point.repository.PointHistoryRepository;
import io.github.dongjulim.domain.point.usecase.FindPointHistoryUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindPointHistoryService implements FindPointHistoryUseCase {

    private final PointHistoryRepository pointHistoryRepository;
    private final UserLoader userLoader;

    @Override
    public Page<FindPointHistoryResponse> findPointHistory(String username, Pageable pageable) {
        User user = userLoader.load(username);
        return pointHistoryRepository.findAllByUserId(user.getId(), pageable)
                .map(FindPointHistoryResponse::from);
    }
}
