package io.github.dongjulim.domain.point.usecase.service;

import io.github.dongjulim.domain.point.dto.FindPointResponse;
import io.github.dongjulim.domain.point.entity.UserPoint;
import io.github.dongjulim.domain.point.repository.UserPointRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindPointServiceTest {

    @Mock
    private UserPointRepository userPointRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private FindPointService findPointService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("findPoint - 포인트가 있으면 잔액을 반환한다")
    void findPoint_shouldReturnBalance_whenPointExists() {
        UserPoint userPoint = UserPoint.builder().userId(1L).balance(1500L).build();
        given(userLoader.load("testuser")).willReturn(user);
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.of(userPoint));

        FindPointResponse response = findPointService.findPoint("testuser");

        assertThat(response.getBalance()).isEqualTo(1500L);
    }

    @Test
    @DisplayName("findPoint - 포인트가 없으면 잔액 0을 반환한다")
    void findPoint_shouldReturnZero_whenPointNotExists() {
        given(userLoader.load("testuser")).willReturn(user);
        given(userPointRepository.findByUserId(1L)).willReturn(Optional.empty());

        FindPointResponse response = findPointService.findPoint("testuser");

        assertThat(response.getBalance()).isEqualTo(0L);
    }
}
