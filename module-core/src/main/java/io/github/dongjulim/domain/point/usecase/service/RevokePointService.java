package io.github.dongjulim.domain.point.usecase.service;

import io.github.dongjulim.domain.point.entity.PointHistory;
import io.github.dongjulim.domain.point.entity.UserPoint;
import io.github.dongjulim.domain.point.enums.PointHistoryType;
import io.github.dongjulim.domain.point.repository.PointHistoryRepository;
import io.github.dongjulim.domain.point.repository.UserPointRepository;
import io.github.dongjulim.domain.point.usecase.RevokePointUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RevokePointService implements RevokePointUseCase {

    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Override
    public void revokePoint(Long userId, Long amount) {
        Optional<UserPoint> optionalUserPoint = userPointRepository.findByUserId(userId);
        if (optionalUserPoint.isEmpty()) {
            return;
        }

        long deducted = optionalUserPoint.get().revoke(amount);

        if (deducted > 0) {
            pointHistoryRepository.save(PointHistory.builder()
                    .userId(userId)
                    .amount(deducted)
                    .type(PointHistoryType.REVOKE)
                    .build());
        }
    }
}
