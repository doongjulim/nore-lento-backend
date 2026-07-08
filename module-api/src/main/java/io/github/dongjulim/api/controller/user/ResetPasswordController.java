package io.github.dongjulim.api.controller.user;

import io.github.dongjulim.domain.user.dto.ResetPasswordRequest;
import io.github.dongjulim.domain.user.usecase.ResetPasswordUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ResetPasswordController {

    private final ResetPasswordUseCase resetPasswordUseCase;

    @PostMapping("/api/v2/user/password/reset")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        resetPasswordUseCase.resetPassword(request.getUsername());
        return ResponseEntity.ok().build();
    }
}
