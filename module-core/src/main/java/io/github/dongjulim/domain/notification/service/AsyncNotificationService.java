package io.github.dongjulim.domain.notification.service;

import io.github.dongjulim.domain.notification.entity.Notification;
import io.github.dongjulim.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AsyncNotificationService {

    private final NotificationRepository notificationRepository;

    @Async("taskExecutor")
    @Transactional
    public void send(Long userId, String title, String content) {
        notificationRepository.save(Notification.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .build());
    }
}
