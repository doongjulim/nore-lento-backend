package io.github.dongjulim.api.controller.user;

import io.github.dongjulim.domain.user.usecase.DeleteUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DeleteUserController {

    private final DeleteUserUseCase deleteUserUseCase;

    @DeleteMapping("/api/v2/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        deleteUserUseCase.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
