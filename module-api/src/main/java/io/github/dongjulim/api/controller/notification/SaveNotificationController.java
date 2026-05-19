package io.github.dongjulim.api.controller.notification;

import io.github.dongjulim.domain.notification.dto.SaveNotificationRequest;
import io.github.dongjulim.domain.notification.usecase.SaveNotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SaveNotificationController {

    private final SaveNotificationUseCase saveNotificationUseCase;

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @PostMapping("/api/v2/notifications")
    public ResponseEntity<Void> saveNotification(
            @RequestBody @Valid SaveNotificationRequest request
    ) {
        saveNotificationUseCase.saveNotification(request);
        return ResponseEntity.ok().build();
    }
}
