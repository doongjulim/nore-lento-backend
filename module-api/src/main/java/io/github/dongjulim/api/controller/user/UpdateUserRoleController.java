package io.github.dongjulim.api.controller.user;

import io.github.dongjulim.domain.user.dto.UpdateUserRoleRequest;
import io.github.dongjulim.domain.user.usecase.UpdateUserRoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UpdateUserRoleController {

    private final UpdateUserRoleUseCase updateUserRoleUseCase;

    @PatchMapping("/api/v2/user/{id}/role")
    public ResponseEntity<Void> updateUserRole(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserRoleRequest request
    ) {
        updateUserRoleUseCase.updateUserRole(id, request);
        return ResponseEntity.ok().build();
    }
}
