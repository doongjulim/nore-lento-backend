package io.github.dongjulim.api.controller.user;

import io.github.dongjulim.domain.user.dto.FindUserDetailResponse;
import io.github.dongjulim.domain.user.usecase.FindUserDetailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindUserDetailController {

    private final FindUserDetailUseCase findUserDetailUseCase;

    @GetMapping("/api/v2/user/{id}")
    public ResponseEntity<FindUserDetailResponse> findUserDetail(@PathVariable Long id) {
        return ResponseEntity.ok(findUserDetailUseCase.findUserDetail(id));
    }
}
