package io.github.dongjulim.domain.notification.usecase.service;

import io.github.dongjulim.domain.common.exception.NotificationNotFoundException;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DeleteNotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private DeleteNotificationService deleteNotificationService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    @DisplayName("deleteNotification - deleteCheck가 true로 변경된다")
    void deleteNotification_shouldSetDeleteCheckTrue() {
        Notification notification = Notification.builder()
                .id(1L).userId(1L).title("주문 완료").content("완료되었습니다.").isRead(false).deleteCheck(false).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(notificationRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(notification));

        deleteNotificationService.deleteNotification(1L, "testuser");

        assertThat(notification.getDeleteCheck()).isTrue();
    }

    @Test
    @DisplayName("deleteNotification - 존재하지 않는 알림이면 NotificationNotFoundException을 던진다")
    void deleteNotification_throwsNotificationNotFoundException_whenNotFound() {
        given(userLoader.load("testuser")).willReturn(user);
        given(notificationRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> deleteNotificationService.deleteNotification(99L, "testuser"))
                .isInstanceOf(NotificationNotFoundException.class);
    }

    @Test
    @DisplayName("deleteNotification - 본인의 알림이 아니면 NotificationNotFoundException을 던진다")
    void deleteNotification_throwsNotificationNotFoundException_whenNotOwner() {
        Notification notification = Notification.builder()
                .id(1L).userId(999L).title("주문 완료").content("완료되었습니다.").isRead(false).deleteCheck(false).build();

        given(userLoader.load("testuser")).willReturn(user);
        given(notificationRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(notification));

        assertThatThrownBy(() -> deleteNotificationService.deleteNotification(1L, "testuser"))
                .isInstanceOf(NotificationNotFoundException.class);
    }
}
