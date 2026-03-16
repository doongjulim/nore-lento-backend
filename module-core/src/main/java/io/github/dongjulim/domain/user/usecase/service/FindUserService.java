package io.github.dongjulim.domain.user.usecase.service;

import io.github.dongjulim.domain.user.dto.FindUserResponse;
import io.github.dongjulim.domain.user.repository.UserRepository;
import io.github.dongjulim.domain.user.usecase.FindUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindUserService implements FindUserUseCase {

    private final UserRepository userRepository;

    @Override
    public List<FindUserResponse> findUser() {
        return userRepository.findAllByDeleteCheckFalse().stream()
                .map(FindUserResponse::from)
                .toList();
    }
}
