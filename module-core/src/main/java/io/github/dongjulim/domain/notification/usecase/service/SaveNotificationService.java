package io.github.dongjulim.domain.notification.usecase.service;

import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.notification.dto.SaveNotificationRequest;
import io.github.dongjulim.domain.notification.entity.Notification;
import io.github.dongjulim.domain.notification.repository.NotificationRepository;
import io.github.dongjulim.domain.notification.usecase.SaveNotificationUseCase;
import io.github.dongjulim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SaveNotificationService implements SaveNotificationUseCase {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public void saveNotification(SaveNotificationRequest request) {
        userRepository.findByIdAndDeleteCheckFalse(request.getUserId())
                .orElseThrow(UserNotFoundException::new);

        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        notificationRepository.save(notification);
    }
}
