package io.github.dongjulim.api.controller.notification;

import io.github.dongjulim.domain.notification.usecase.ReadAllNotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReadAllNotificationController {

    private final ReadAllNotificationUseCase readAllNotificationUseCase;

    @PatchMapping("/api/v2/notifications/read-all")
    public ResponseEntity<Void> readAll(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        readAllNotificationUseCase.readAll(userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
