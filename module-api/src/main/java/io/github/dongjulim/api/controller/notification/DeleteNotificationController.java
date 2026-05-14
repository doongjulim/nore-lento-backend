package io.github.dongjulim.api.controller.notification;

import io.github.dongjulim.domain.notification.usecase.DeleteNotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteNotificationController {

    private final DeleteNotificationUseCase deleteNotificationUseCase;

    @DeleteMapping("/api/v2/notifications/{id}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        deleteNotificationUseCase.deleteNotification(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
