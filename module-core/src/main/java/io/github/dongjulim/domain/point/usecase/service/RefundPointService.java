package io.github.dongjulim.domain.point.usecase.service;

import io.github.dongjulim.domain.point.entity.PointHistory;
import io.github.dongjulim.domain.point.entity.UserPoint;
import io.github.dongjulim.domain.point.enums.PointHistoryType;
import io.github.dongjulim.domain.point.repository.PointHistoryRepository;
import io.github.dongjulim.domain.point.repository.UserPointRepository;
import io.github.dongjulim.domain.point.usecase.RefundPointUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RefundPointService implements RefundPointUseCase {

    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Override
    public void refundPoint(Long userId, Long amount) {
        UserPoint userPoint = userPointRepository.findByUserId(userId)
                .orElseGet(() -> userPointRepository.save(
                        UserPoint.builder().userId(userId).balance(0L).build()
                ));
        userPoint.earn(amount);
        pointHistoryRepository.save(PointHistory.builder()
                .userId(userId)
                .amount(amount)
                .type(PointHistoryType.REFUND)
                .build());
    }
}
