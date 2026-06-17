package io.github.dongjulim.domain.notification.repository;

import io.github.dongjulim.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByIdAndDeleteCheckFalse(Long id);

    Page<Notification> findAllByUserIdAndDeleteCheckFalse(Long userId, Pageable pageable);

    List<Notification> findAllByUserIdAndDeleteCheckFalseAndIsReadFalse(Long userId);
}
