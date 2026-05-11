package io.github.dongjulim.api.controller.review;

import io.github.dongjulim.domain.review.dto.SaveReviewRequest;
import io.github.dongjulim.domain.review.usecase.SaveReviewUseCase;
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
public class SaveReviewController {

    private final SaveReviewUseCase saveReviewUseCase;

    @PostMapping("/api/v2/reviews")
    public ResponseEntity<Void> saveReview(
            @RequestBody @Valid SaveReviewRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        saveReviewUseCase.saveReview(request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
