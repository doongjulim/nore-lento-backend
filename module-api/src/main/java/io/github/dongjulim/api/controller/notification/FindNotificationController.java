package io.github.dongjulim.api.controller.notification;

import io.github.dongjulim.domain.notification.dto.FindNotificationResponse;
import io.github.dongjulim.domain.notification.usecase.FindNotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindNotificationController {

    private final FindNotificationUseCase findNotificationUseCase;

    @GetMapping("/api/v2/notifications")
    public ResponseEntity<Page<FindNotificationResponse>> findNotifications(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(findNotificationUseCase.findNotifications(userDetails.getUsername(), pageable));
    }
}
