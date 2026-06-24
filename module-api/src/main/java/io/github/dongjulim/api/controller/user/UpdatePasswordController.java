package io.github.dongjulim.api.controller.user;

import io.github.dongjulim.domain.user.dto.UpdatePasswordRequest;
import io.github.dongjulim.domain.user.usecase.UpdatePasswordUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdatePasswordController {

    private final UpdatePasswordUseCase updatePasswordUseCase;

    @PatchMapping("/api/v2/users/{id}/password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable Long id,
            @RequestBody UpdatePasswordRequest request
    ) {
        updatePasswordUseCase.updatePassword(id, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
