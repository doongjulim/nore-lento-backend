package io.github.dongjulim.domain.point.usecase.service;

import io.github.dongjulim.domain.point.entity.PointHistory;
import io.github.dongjulim.domain.point.entity.UserPoint;
import io.github.dongjulim.domain.point.repository.PointHistoryRepository;
import io.github.dongjulim.domain.point.repository.UserPointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class EarnPointServiceTest {

    @Mock
    private UserPointRepository userPointRepository;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @InjectMocks
    private EarnPointService earnPointService;

    @Test
    @DisplayName("earnPoint - 처음 적립 시 UserPoint를 생성하고 총액의 1%를 적립한다")
    void earnPoint_shouldCreateUserPointAndEarn_whenFirstTime() {
        UserPoint newPoint = UserPoint.builder().userId(1L).balance(0L).build();
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.empty());
        given(userPointRepository.save(any(UserPoint.class))).willReturn(newPoint);

        earnPointService.earnPoint(1L, 10000L);

        assertThat(newPoint.getBalance()).isEqualTo(100L); // 1% of 10000
    }

    @Test
    @DisplayName("earnPoint - 기존 포인트가 있으면 잔액에 추가 적립한다")
    void earnPoint_shouldAddToExistingBalance() {
        UserPoint existing = UserPoint.builder().id(1L).userId(1L).balance(500L).build();
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.of(existing));

        earnPointService.earnPoint(1L, 5000L);

        assertThat(existing.getBalance()).isEqualTo(550L); // 500 + 50 (1% of 5000)
    }

    @Test
    @DisplayName("earnPoint - 포인트 적립 시 적립 이력이 저장된다")
    void earnPoint_shouldSaveHistory_whenPointsEarned() {
        UserPoint existing = UserPoint.builder().id(1L).userId(1L).balance(500L).build();
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.of(existing));

        earnPointService.earnPoint(1L, 5000L);

        then(pointHistoryRepository).should().save(any(PointHistory.class));
    }

    @Test
    @DisplayName("earnPoint - 적립 포인트가 0이면 아무 작업도 하지 않는다")
    void earnPoint_shouldDoNothing_whenEarnedPointsIsZero() {
        earnPointService.earnPoint(1L, 50L); // 1% of 50 = 0

        then(userPointRepository).shouldHaveNoInteractions();
        then(pointHistoryRepository).shouldHaveNoInteractions();
    }
}
