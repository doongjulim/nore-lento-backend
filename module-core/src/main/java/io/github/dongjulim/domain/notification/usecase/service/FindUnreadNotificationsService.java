package io.github.dongjulim.domain.notification.usecase.service;

import io.github.dongjulim.domain.notification.dto.FindNotificationResponse;
import io.github.dongjulim.domain.notification.repository.NotificationRepository;
import io.github.dongjulim.domain.notification.usecase.FindUnreadNotificationsUseCase;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindUnreadNotificationsService implements FindUnreadNotificationsUseCase {

    private final NotificationRepository notificationRepository;
    private final UserLoader userLoader;

    @Override
    public Page<FindNotificationResponse> findUnreadNotifications(String username, Pageable pageable) {
        User user = userLoader.load(username);
        return notificationRepository.findAllByUserIdAndDeleteCheckFalseAndIsReadFalse(user.getId(), pageable)
                .map(FindNotificationResponse::from);
    }
}
