package io.github.dongjulim.domain.notification.usecase.service;

import io.github.dongjulim.domain.notification.repository.NotificationRepository;
import io.github.dongjulim.domain.notification.usecase.ReadAllNotificationUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadAllNotificationService implements ReadAllNotificationUseCase {

    private final NotificationRepository notificationRepository;
    private final UserLoader userLoader;

    @Override
    public void readAll(String username) {
        User user = userLoader.load(username);

        notificationRepository.findAllByUserIdAndDeleteCheckFalseAndIsReadFalse(user.getId())
                .forEach(notification -> notification.markAsRead());
    }
}
