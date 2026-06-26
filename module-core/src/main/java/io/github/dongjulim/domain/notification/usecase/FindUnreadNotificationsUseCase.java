package io.github.dongjulim.domain.notification.usecase;

import io.github.dongjulim.domain.notification.dto.FindNotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindUnreadNotificationsUseCase {
    Page<FindNotificationResponse> findUnreadNotifications(String username, Pageable pageable);
}
