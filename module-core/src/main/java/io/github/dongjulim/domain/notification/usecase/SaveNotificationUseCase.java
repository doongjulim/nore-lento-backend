package io.github.dongjulim.domain.notification.usecase;

import io.github.dongjulim.domain.notification.dto.SaveNotificationRequest;

public interface SaveNotificationUseCase {
    void saveNotification(SaveNotificationRequest request);
}
