package io.github.dongjulim.domain.point.usecase.service;

import io.github.dongjulim.domain.common.exception.InsufficientPointException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UsePointServiceTest {

    @Mock
    private UserPointRepository userPointRepository;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @InjectMocks
    private UsePointService usePointService;

    @Test
    @DisplayName("usePoint - 포인트 사용 시 잔액이 차감된다")
    void usePoint_shouldDeductBalance() {
        UserPoint userPoint = UserPoint.builder().id(1L).userId(1L).balance(1000L).build();
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.of(userPoint));

        usePointService.usePoint(1L, 300L);

        assertThat(userPoint.getBalance()).isEqualTo(700L);
    }

    @Test
    @DisplayName("usePoint - 포인트 사용 시 USE 이력이 저장된다")
    void usePoint_shouldSaveUseHistory() {
        UserPoint userPoint = UserPoint.builder().id(1L).userId(1L).balance(1000L).build();
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.of(userPoint));

        usePointService.usePoint(1L, 300L);

        then(pointHistoryRepository).should().save(any(PointHistory.class));
    }

    @Test
    @DisplayName("usePoint - UserPoint가 없으면 InsufficientPointException이 발생한다")
    void usePoint_shouldThrow_whenUserPointNotFound() {
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> usePointService.usePoint(1L, 300L))
                .isInstanceOf(InsufficientPointException.class);
    }

    @Test
    @DisplayName("usePoint - 잔액보다 많은 포인트 사용 시 InsufficientPointException이 발생한다")
    void usePoint_shouldThrow_whenBalanceInsufficient() {
        UserPoint userPoint = UserPoint.builder().id(1L).userId(1L).balance(100L).build();
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.of(userPoint));

        assertThatThrownBy(() -> usePointService.usePoint(1L, 500L))
                .isInstanceOf(InsufficientPointException.class);
    }
}
