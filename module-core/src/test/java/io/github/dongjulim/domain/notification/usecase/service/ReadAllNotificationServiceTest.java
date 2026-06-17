package io.github.dongjulim.domain.notification.usecase.service;

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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReadAllNotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private ReadAllNotificationService readAllNotificationService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("readAll - 미읽음 알림이 모두 isRead=true가 된다")
    void readAll_shouldMarkAllUnreadNotificationsAsRead() {
        Notification n1 = Notification.builder().id(1L).userId(1L).title("알림1").content("내용1").isRead(false).deleteCheck(false).build();
        Notification n2 = Notification.builder().id(2L).userId(1L).title("알림2").content("내용2").isRead(false).deleteCheck(false).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(notificationRepository.findAllByUserIdAndDeleteCheckFalseAndIsReadFalse(1L)).willReturn(List.of(n1, n2));

        readAllNotificationService.readAll("testuser");

        assertThat(n1.getIsRead()).isTrue();
        assertThat(n2.getIsRead()).isTrue();
    }

    @Test
    @DisplayName("readAll - 미읽음 알림이 없어도 예외가 발생하지 않는다")
    void readAll_shouldNotThrow_whenNoUnreadNotifications() {
        given(userLoader.load("testuser")).willReturn(user);
        given(notificationRepository.findAllByUserIdAndDeleteCheckFalseAndIsReadFalse(1L)).willReturn(List.of());

        readAllNotificationService.readAll("testuser");
    }
}
