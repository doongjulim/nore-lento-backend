package io.github.dongjulim.api.controller.notice;

import io.github.dongjulim.domain.notice.dto.FindNoticeRequest;
import io.github.dongjulim.domain.notice.dto.FindNoticeResponse;
import io.github.dongjulim.domain.notice.usecase.FindNoticeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/notice")
public class FindNoticeController {

    private final FindNoticeUseCase findNoticeUseCase;

    @GetMapping
    public ResponseEntity<Page<FindNoticeResponse>> findNotices(
            @ModelAttribute FindNoticeRequest request,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(findNoticeUseCase.findNotice(request, pageable));
    }
}
