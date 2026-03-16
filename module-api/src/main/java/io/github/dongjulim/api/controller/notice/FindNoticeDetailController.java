package io.github.dongjulim.api.controller.notice;

import io.github.dongjulim.domain.notice.dto.FindNoticeDetailResponse;
import io.github.dongjulim.domain.notice.usecase.FindNoticeDetailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindNoticeDetailController {

    private final FindNoticeDetailUseCase findNoticeDetailUseCase;

    @GetMapping("/api/v2/notice/{id}")
    public ResponseEntity<FindNoticeDetailResponse> findNoticeDetail(@PathVariable Long id) {
        return ResponseEntity.ok(findNoticeDetailUseCase.findNoticeDetail(id));
    }
}
