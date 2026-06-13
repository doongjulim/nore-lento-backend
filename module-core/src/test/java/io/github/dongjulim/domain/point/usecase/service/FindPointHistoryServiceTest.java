package io.github.dongjulim.domain.point.usecase.service;

import io.github.dongjulim.domain.point.dto.FindPointHistoryResponse;
import io.github.dongjulim.domain.point.entity.PointHistory;
import io.github.dongjulim.domain.point.enums.PointHistoryType;
import io.github.dongjulim.domain.point.repository.PointHistoryRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindPointHistoryServiceTest {

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private FindPointHistoryService findPointHistoryService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("findPointHistory - 포인트 이력 목록을 페이지네이션으로 반환한다")
    void findPointHistory_shouldReturnPagedHistory() {
        Pageable pageable = PageRequest.of(0, 10);
        List<PointHistory> histories = List.of(
                PointHistory.builder().id(1L).userId(1L).amount(100L).type(PointHistoryType.EARN).build(),
                PointHistory.builder().id(2L).userId(1L).amount(500L).type(PointHistoryType.USE).build()
        );
        given(userLoader.load("testuser")).willReturn(user);
        given(pointHistoryRepository.findAllByUserId(1L, pageable))
                .willReturn(new PageImpl<>(histories, pageable, 2));

        Page<FindPointHistoryResponse> result = findPointHistoryService.findPointHistory("testuser", pageable);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getAmount()).isEqualTo(100L);
        assertThat(result.getContent().get(0).getType()).isEqualTo(PointHistoryType.EARN);
        assertThat(result.getContent().get(1).getAmount()).isEqualTo(500L);
        assertThat(result.getContent().get(1).getType()).isEqualTo(PointHistoryType.USE);
    }

    @Test
    @DisplayName("findPointHistory - 이력이 없으면 빈 페이지를 반환한다")
    void findPointHistory_shouldReturnEmptyPage_whenNoHistory() {
        Pageable pageable = PageRequest.of(0, 10);
        given(userLoader.load("testuser")).willReturn(user);
        given(pointHistoryRepository.findAllByUserId(1L, pageable))
                .willReturn(Page.empty(pageable));

        Page<FindPointHistoryResponse> result = findPointHistoryService.findPointHistory("testuser", pageable);

        assertThat(result.isEmpty()).isTrue();
    }
}
