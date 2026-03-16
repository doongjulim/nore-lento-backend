package io.github.dongjulim.api.controller.notice;

import io.github.dongjulim.domain.notice.dto.UpdateNoticeRequest;
import io.github.dongjulim.domain.notice.usecase.UpdateNoticeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UpdateNoticeController {

    private final UpdateNoticeUseCase updateNoticeUseCase;

    @PutMapping("/api/v2/notice/{id}")
    public ResponseEntity<Void> updateNotice(
            @PathVariable Long id,
            @RequestBody @Valid UpdateNoticeRequest request
    ) {
        updateNoticeUseCase.updateNotice(id, request);
        return ResponseEntity.ok().build();
    }
}
