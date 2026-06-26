package io.github.dongjulim.api.controller.notification;

import io.github.dongjulim.domain.notification.dto.FindNotificationResponse;
import io.github.dongjulim.domain.notification.usecase.FindUnreadNotificationsUseCase;
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
public class FindUnreadNotificationsController {

    private final FindUnreadNotificationsUseCase findUnreadNotificationsUseCase;

    @GetMapping("/api/v2/notifications/unread")
    public ResponseEntity<Page<FindNotificationResponse>> findUnreadNotifications(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(findUnreadNotificationsUseCase.findUnreadNotifications(userDetails.getUsername(), pageable));
    }
}
