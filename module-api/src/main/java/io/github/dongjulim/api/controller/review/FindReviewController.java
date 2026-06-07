package io.github.dongjulim.api.controller.review;

import io.github.dongjulim.domain.review.dto.FindReviewResponse;
import io.github.dongjulim.domain.review.usecase.FindReviewUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindReviewController {

    private final FindReviewUseCase findReviewUseCase;

    @GetMapping("/api/v2/reviews")
    public ResponseEntity<Page<FindReviewResponse>> findReviews(
            @RequestParam Long productId,
            @RequestParam(required = false) Integer rating,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(findReviewUseCase.findReviews(productId, rating, pageable));
    }
}
