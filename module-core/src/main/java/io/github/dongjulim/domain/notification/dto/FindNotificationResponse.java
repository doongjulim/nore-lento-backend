package io.github.dongjulim.domain.notification.dto;

import io.github.dongjulim.domain.notification.entity.Notification;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindNotificationResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final Boolean isRead;
    private final LocalDateTime createAt;

    public static FindNotificationResponse from(Notification notification) {
        return FindNotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .isRead(notification.getIsRead())
                .createAt(notification.getCreateAt())
                .build();
    }
}
