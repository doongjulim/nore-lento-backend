package io.github.dongjulim.api.controller.user;

import io.github.dongjulim.domain.user.dto.SaveUserRequest;
import io.github.dongjulim.domain.user.usecase.SaveUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SaveUserController {

    private final SaveUserUseCase saveUserUseCase;

    @PostMapping("/api/v2/user")
    public ResponseEntity<Void> saveUser(@RequestBody @Valid SaveUserRequest request) {
        saveUserUseCase.saveUser(request);
        return ResponseEntity.ok().build();
    }
}
