package io.github.dongjulim.api.controller.point;

import io.github.dongjulim.domain.point.dto.FindPointResponse;
import io.github.dongjulim.domain.point.usecase.FindPointUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindPointController {

    private final FindPointUseCase findPointUseCase;

    @GetMapping("/api/v2/points")
    public ResponseEntity<FindPointResponse> findPoint(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(findPointUseCase.findPoint(userDetails.getUsername()));
    }
}
