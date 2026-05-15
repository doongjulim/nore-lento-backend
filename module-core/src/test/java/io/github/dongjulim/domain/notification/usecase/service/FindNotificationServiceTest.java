package io.github.dongjulim.domain.notification.usecase.service;

import io.github.dongjulim.domain.notification.dto.FindNotificationResponse;
import io.github.dongjulim.domain.notification.entity.Notification;
import io.github.dongjulim.domain.notification.repository.NotificationRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
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
class FindNotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private FindNotificationService findNotificationService;

    @Test
    @DisplayName("findNotifications - 내 알림 목록을 페이징하여 반환한다")
    void findNotifications_shouldReturnPagedNotifications() {
        User user = User.builder().id(1L).username("testuser").build();
        Notification notification1 = Notification.builder()
                .id(1L).userId(1L).title("주문 완료").content("완료되었습니다.").isRead(false).deleteCheck(false).build();
        Notification notification2 = Notification.builder()
                .id(2L).userId(1L).title("배송 시작").content("출발했습니다.").isRead(true).deleteCheck(false).build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Notification> notificationPage = new PageImpl<>(List.of(notification1, notification2), pageable, 2);

        given(userLoader.load("testuser")).willReturn(user);
        given(notificationRepository.findAllByUserIdAndDeleteCheckFalse(1L, pageable)).willReturn(notificationPage);

        Page<FindNotificationResponse> result = findNotificationService.findNotifications("testuser", pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("주문 완료");
        assertThat(result.getContent().get(0).getIsRead()).isFalse();
        assertThat(result.getContent().get(1).getTitle()).isEqualTo("배송 시작");
        assertThat(result.getContent().get(1).getIsRead()).isTrue();
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("findNotifications - 알림이 없으면 빈 페이지를 반환한다")
    void findNotifications_shouldReturnEmptyPage_whenNoNotifications() {
        User user = User.builder().id(1L).username("testuser").build();
        Pageable pageable = PageRequest.of(0, 10);

        given(userLoader.load("testuser")).willReturn(user);
        given(notificationRepository.findAllByUserIdAndDeleteCheckFalse(1L, pageable)).willReturn(Page.empty(pageable));

        Page<FindNotificationResponse> result = findNotificationService.findNotifications("testuser", pageable);

        assertThat(result.getContent()).isEmpty();
    }
}
