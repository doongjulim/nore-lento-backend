package io.github.dongjulim.domain.point.usecase.service;

import io.github.dongjulim.domain.common.exception.InsufficientPointException;
import io.github.dongjulim.domain.point.entity.PointHistory;
import io.github.dongjulim.domain.point.entity.UserPoint;
import io.github.dongjulim.domain.point.enums.PointHistoryType;
import io.github.dongjulim.domain.point.repository.PointHistoryRepository;
import io.github.dongjulim.domain.point.repository.UserPointRepository;
import io.github.dongjulim.domain.point.usecase.UsePointUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UsePointService implements UsePointUseCase {

    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Override
    public void usePoint(Long userId, Long amount) {
        UserPoint userPoint = userPointRepository.findByUserId(userId)
                .orElseThrow(InsufficientPointException::new);
        userPoint.use(amount);
        pointHistoryRepository.save(PointHistory.builder()
                .userId(userId)
                .amount(amount)
                .type(PointHistoryType.USE)
                .build());
    }
}
