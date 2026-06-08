package io.github.dongjulim.domain.point.usecase.service;

import io.github.dongjulim.domain.point.entity.UserPoint;
import io.github.dongjulim.domain.point.repository.UserPointRepository;
import io.github.dongjulim.domain.point.usecase.EarnPointUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EarnPointService implements EarnPointUseCase {

    private final UserPointRepository userPointRepository;

    @Override
    public void earnPoint(Long userId, Long totalPrice) {
        long earned = totalPrice / 100; // 1%
        if (earned == 0) return;

        UserPoint userPoint = userPointRepository.findByUserId(userId)
                .orElseGet(() -> userPointRepository.save(
                        UserPoint.builder().userId(userId).balance(0L).build()
                ));

        userPoint.earn(earned);
    }
}
