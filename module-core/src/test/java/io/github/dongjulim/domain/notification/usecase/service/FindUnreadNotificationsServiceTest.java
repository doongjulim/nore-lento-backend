package io.github.dongjulim.domain.notification.usecase.service;

import io.github.dongjulim.domain.notification.dto.FindNotificationResponse;
import io.github.dongjulim.domain.notification.entity.Notification;
import io.github.dongjulim.domain.notification.repository.NotificationRepository;
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
class FindUnreadNotificationsServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private FindUnreadNotificationsService findUnreadNotificationsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("findUnreadNotifications - 미읽음 알림 목록을 페이지로 반환한다")
    void findUnreadNotifications_shouldReturnUnreadNotifications() {
        Notification n1 = Notification.builder().id(1L).userId(1L).title("알림1").content("내용1").isRead(false).deleteCheck(false).build();
        Notification n2 = Notification.builder().id(2L).userId(1L).title("알림2").content("내용2").isRead(false).deleteCheck(false).build();
        Pageable pageable = PageRequest.of(0, 10);

        given(userLoader.load("testuser")).willReturn(user);
        given(notificationRepository.findAllByUserIdAndDeleteCheckFalseAndIsReadFalse(1L, pageable))
                .willReturn(new PageImpl<>(List.of(n1, n2)));

        Page<FindNotificationResponse> result = findUnreadNotificationsService.findUnreadNotifications("testuser", pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("알림1");
        assertThat(result.getContent().get(1).getTitle()).isEqualTo("알림2");
    }

    @Test
    @DisplayName("findUnreadNotifications - 미읽음 알림이 없으면 빈 페이지를 반환한다")
    void findUnreadNotifications_shouldReturnEmptyPage_whenNoUnread() {
        Pageable pageable = PageRequest.of(0, 10);

        given(userLoader.load("testuser")).willReturn(user);
        given(notificationRepository.findAllByUserIdAndDeleteCheckFalseAndIsReadFalse(1L, pageable))
                .willReturn(Page.empty());

        Page<FindNotificationResponse> result = findUnreadNotificationsService.findUnreadNotifications("testuser", pageable);

        assertThat(result.getContent()).isEmpty();
    }
}
