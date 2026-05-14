package io.github.dongjulim.domain.notification.usecase.service;

import io.github.dongjulim.domain.common.exception.NotificationNotFoundException;
import io.github.dongjulim.domain.notification.entity.Notification;
import io.github.dongjulim.domain.notification.repository.NotificationRepository;
import io.github.dongjulim.domain.notification.usecase.DeleteNotificationUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteNotificationService implements DeleteNotificationUseCase {

    private final NotificationRepository notificationRepository;
    private final UserLoader userLoader;

    @Override
    public void deleteNotification(Long id, String username) {
        User user = userLoader.load(username);

        Notification notification = notificationRepository.findByIdAndDeleteCheckFalse(id)
                .filter(n -> n.getUserId().equals(user.getId()))
                .orElseThrow(NotificationNotFoundException::new);

        notification.deleteNotification();
    }
}
