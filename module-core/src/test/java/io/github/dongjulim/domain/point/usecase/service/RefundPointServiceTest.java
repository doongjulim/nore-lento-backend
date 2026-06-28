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
class RefundPointServiceTest {

    @Mock
    private UserPointRepository userPointRepository;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @InjectMocks
    private RefundPointService refundPointService;

    @Test
    @DisplayName("refundPoint - 포인트 환불 시 잔액에 추가된다")
    void refundPoint_shouldAddBalance() {
        UserPoint userPoint = UserPoint.builder().id(1L).userId(1L).balance(200L).build();
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.of(userPoint));

        refundPointService.refundPoint(1L, 300L);

        assertThat(userPoint.getBalance()).isEqualTo(500L);
    }

    @Test
    @DisplayName("refundPoint - 포인트 환불 시 REFUND 이력이 저장된다")
    void refundPoint_shouldSaveRefundHistory() {
        UserPoint userPoint = UserPoint.builder().id(1L).userId(1L).balance(200L).build();
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.of(userPoint));

        refundPointService.refundPoint(1L, 300L);

        then(pointHistoryRepository).should().save(any(PointHistory.class));
    }

    @Test
    @DisplayName("refundPoint - UserPoint가 없으면 새로 생성하고 환불한다")
    void refundPoint_shouldCreateUserPoint_whenNotExists() {
        UserPoint newPoint = UserPoint.builder().userId(1L).balance(0L).build();
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.empty());
        given(userPointRepository.save(any(UserPoint.class))).willReturn(newPoint);

        refundPointService.refundPoint(1L, 300L);

        assertThat(newPoint.getBalance()).isEqualTo(300L);
    }
}
