package io.github.dongjulim.api.controller.notification;

import io.github.dongjulim.domain.notification.usecase.ReadNotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReadNotificationController {

    private final ReadNotificationUseCase readNotificationUseCase;

    @PutMapping("/api/v2/notifications/{id}/read")
    public ResponseEntity<Void> readNotification(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        readNotificationUseCase.readNotification(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
