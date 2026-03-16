package io.github.dongjulim.api.controller.notice;

import io.github.dongjulim.domain.notice.dto.FindNoticeResponse;
import io.github.dongjulim.domain.notice.usecase.FindNoticeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FindNoticeController {

    private final FindNoticeUseCase findNoticeUseCase;

    @GetMapping("/api/v2/notice")
    public ResponseEntity<List<FindNoticeResponse>> findNotices() {
        return ResponseEntity.ok(findNoticeUseCase.findNotice());
    }
}
