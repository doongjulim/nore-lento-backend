package io.github.dongjulim.api.controller.notice;

import io.github.dongjulim.domain.notice.usecase.LikeNoticeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeNoticeController {

    private final LikeNoticeUseCase likeNoticeUseCase;

    @PostMapping("/api/v2/notice/{id}/like")
    public ResponseEntity<Void> likeNotice(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        likeNoticeUseCase.likeNotice(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
