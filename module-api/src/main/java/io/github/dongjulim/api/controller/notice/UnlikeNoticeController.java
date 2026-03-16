package io.github.dongjulim.api.controller.notice;

import io.github.dongjulim.domain.notice.usecase.UnlikeNoticeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UnlikeNoticeController {

    private final UnlikeNoticeUseCase unlikeNoticeUseCase;

    @DeleteMapping("/api/v2/notice/{id}/like")
    public ResponseEntity<Void> unlikeNotice(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        unlikeNoticeUseCase.unlikeNotice(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
