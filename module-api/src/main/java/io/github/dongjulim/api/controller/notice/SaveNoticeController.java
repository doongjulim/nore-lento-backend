package io.github.dongjulim.api.controller.notice;

import io.github.dongjulim.domain.notice.dto.SaveNoticeRequest;
import io.github.dongjulim.domain.notice.usecase.SaveNoticeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SaveNoticeController {

    private final SaveNoticeUseCase saveNoticeUseCase;

    @PostMapping("/api/v2/notice")
    public ResponseEntity<Void> saveNotice(
            @RequestBody @Valid SaveNoticeRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        saveNoticeUseCase.saveNotice(request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
