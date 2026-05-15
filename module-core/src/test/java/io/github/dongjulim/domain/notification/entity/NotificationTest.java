package io.github.dongjulim.domain.notification.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationTest {

    private Notification createNotification() {
        return Notification.builder()
                .id(1L)
                .userId(1L)
                .title("주문 완료")
                .content("주문이 정상적으로 완료되었습니다.")
                .isRead(false)
                .deleteCheck(false)
                .build();
    }

    @Test
    @DisplayName("markAsRead - isRead가 true로 변경된다")
    void markAsRead_shouldSetIsReadTrue() {
        Notification notification = createNotification();

        notification.markAsRead();

        assertThat(notification.getIsRead()).isTrue();
    }

    @Test
    @DisplayName("markAsRead - 읽음 처리 후 deleteCheck는 유지된다")
    void markAsRead_shouldNotChangeDeleteCheck() {
        Notification notification = createNotification();

        notification.markAsRead();

        assertThat(notification.getDeleteCheck()).isFalse();
    }

    @Test
    @DisplayName("deleteNotification - deleteCheck가 true로 변경된다")
    void deleteNotification_shouldSetDeleteCheckTrue() {
        Notification notification = createNotification();

        notification.deleteNotification();

        assertThat(notification.getDeleteCheck()).isTrue();
    }
}
