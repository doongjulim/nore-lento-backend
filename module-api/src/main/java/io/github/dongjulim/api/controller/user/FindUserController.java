package io.github.dongjulim.api.controller.user;

import io.github.dongjulim.domain.user.dto.FindUserResponse;
import io.github.dongjulim.domain.user.usecase.FindUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

@RequiredArgsConstructor
public class FindUserController {

    private final FindUserUseCase findUserUseCase;

    @GetMapping("/api/v2/user")
    public ResponseEntity<List<FindUserResponse>> findUsers() {
        return ResponseEntity.ok(findUserUseCase.findUser());
    }

}
