package io.github.dongjulim.api.controller.user;

import io.github.dongjulim.domain.user.dto.UpdateUserGradeRequest;
import io.github.dongjulim.domain.user.usecase.UpdateUserGradeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UpdateUserGradeController {

    private final UpdateUserGradeUseCase updateUserGradeUseCase;

    @PatchMapping("/api/v2/user/{id}/grade")
    public ResponseEntity<Void> updateUserGrade(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserGradeRequest request
    ) {
        updateUserGradeUseCase.updateUserGrade(id, request);
        return ResponseEntity.ok().build();
    }
}
