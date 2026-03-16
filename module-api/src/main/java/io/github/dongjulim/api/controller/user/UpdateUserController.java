package io.github.dongjulim.api.controller.user;

import io.github.dongjulim.domain.user.dto.UpdateUserRequest;
import io.github.dongjulim.domain.user.usecase.UpdateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class UpdateUserController {

    private final UpdateUserUseCase updateUserUseCase;

    @PutMapping("/api/v2/user/{id}")
    public ResponseEntity<Void> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserRequest request
    ) {
        updateUserUseCase.updateUser(id, request);
        return ResponseEntity.ok().build();
    }
}
