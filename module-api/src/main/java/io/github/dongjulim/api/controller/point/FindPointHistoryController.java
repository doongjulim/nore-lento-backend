package io.github.dongjulim.api.controller.point;

import io.github.dongjulim.domain.point.dto.FindPointHistoryResponse;
import io.github.dongjulim.domain.point.usecase.FindPointHistoryUseCase;
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
public class FindPointHistoryController {

    private final FindPointHistoryUseCase findPointHistoryUseCase;

    @GetMapping("/api/v2/points/history")
    public ResponseEntity<Page<FindPointHistoryResponse>> findPointHistory(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(findPointHistoryUseCase.findPointHistory(userDetails.getUsername(), pageable));
    }
}
