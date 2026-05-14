package io.github.dongjulim.domain.notification.usecase;

import io.github.dongjulim.domain.notification.dto.FindNotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindNotificationUseCase {
    Page<FindNotificationResponse> findNotifications(String username, Pageable pageable);
}
