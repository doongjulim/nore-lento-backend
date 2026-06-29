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
class RevokePointServiceTest {

    @Mock
    private UserPointRepository userPointRepository;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @InjectMocks
    private RevokePointService revokePointService;

    @Test
    @DisplayName("revokePoint - 잔액이 충분하면 요청 금액 전부 회수되고 이력이 저장된다")
    void revokePoint_shouldDeductFullAmount_whenBalanceSufficient() {
        UserPoint userPoint = UserPoint.builder().id(1L).userId(1L).balance(500L).build();
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.of(userPoint));

        revokePointService.revokePoint(1L, 300L);

        assertThat(userPoint.getBalance()).isEqualTo(200L);
        then(pointHistoryRepository).should().save(any(PointHistory.class));
    }

    @Test
    @DisplayName("revokePoint - 잔액보다 회수 금액이 크면 잔액만큼만 회수되고 이력이 저장된다")
    void revokePoint_shouldDeductPartialAmount_whenBalanceInsufficient() {
        UserPoint userPoint = UserPoint.builder().id(1L).userId(1L).balance(100L).build();
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.of(userPoint));

        revokePointService.revokePoint(1L, 300L);

        assertThat(userPoint.getBalance()).isEqualTo(0L);
        then(pointHistoryRepository).should().save(any(PointHistory.class));
    }

    @Test
    @DisplayName("revokePoint - 잔액이 0이면 회수하지 않고 이력도 저장하지 않는다")
    void revokePoint_shouldDoNothing_whenBalanceIsZero() {
        UserPoint userPoint = UserPoint.builder().id(1L).userId(1L).balance(0L).build();
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.of(userPoint));

        revokePointService.revokePoint(1L, 300L);

        assertThat(userPoint.getBalance()).isEqualTo(0L);
        then(pointHistoryRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("revokePoint - UserPoint가 없으면 아무 작업도 하지 않는다")
    void revokePoint_shouldDoNothing_whenUserPointNotFound() {
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.empty());

        revokePointService.revokePoint(1L, 300L);

        then(pointHistoryRepository).shouldHaveNoInteractions();
    }
}
