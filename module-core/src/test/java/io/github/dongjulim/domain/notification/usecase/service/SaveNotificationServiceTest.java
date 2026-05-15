package io.github.dongjulim.domain.notification.usecase.service;

import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.notification.dto.SaveNotificationRequest;
import io.github.dongjulim.domain.notification.entity.Notification;
import io.github.dongjulim.domain.notification.repository.NotificationRepository;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SaveNotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SaveNotificationService saveNotificationService;

    @Test
    @DisplayName("saveNotification - 알림이 정상적으로 저장된다")
    void saveNotification_shouldSaveNotification() {
        User user = User.builder().id(1L).username("testuser").build();
        SaveNotificationRequest request = new SaveNotificationRequest();
        ReflectionTestUtils.setField(request, "userId", 1L);
        ReflectionTestUtils.setField(request, "title", "주문 완료");
        ReflectionTestUtils.setField(request, "content", "주문이 완료되었습니다.");

        given(userRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(user));

        saveNotificationService.saveNotification(request);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        then(notificationRepository).should().save(captor.capture());
        assertThat(captor.getValue().getUserId()).isEqualTo(1L);
        assertThat(captor.getValue().getTitle()).isEqualTo("주문 완료");
        assertThat(captor.getValue().getContent()).isEqualTo("주문이 완료되었습니다.");
        assertThat(captor.getValue().getIsRead()).isFalse();
    }

    @Test
    @DisplayName("saveNotification - 존재하지 않는 유저면 UserNotFoundException을 던진다")
    void saveNotification_throwsUserNotFoundException_whenUserNotFound() {
        SaveNotificationRequest request = new SaveNotificationRequest();
        ReflectionTestUtils.setField(request, "userId", 99L);
        ReflectionTestUtils.setField(request, "title", "주문 완료");
        ReflectionTestUtils.setField(request, "content", "주문이 완료되었습니다.");

        given(userRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> saveNotificationService.saveNotification(request))
                .isInstanceOf(UserNotFoundException.class);
    }
}
