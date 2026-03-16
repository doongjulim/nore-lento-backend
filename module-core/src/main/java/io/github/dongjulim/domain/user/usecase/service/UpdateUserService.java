package io.github.dongjulim.domain.user.usecase.service;

import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.user.dto.UpdateUserRequest;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.user.repository.UserRepository;
import io.github.dongjulim.domain.user.usecase.UpdateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateUserService implements UpdateUserUseCase {

    private final UserRepository userRepository;

    @Override
    public void updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findByIdAndDeleteCheckFalse(id)
                .orElseThrow(UserNotFoundException::new);
        user.updateUser(request.getUsername(), request.getName(), request.getRole(), request.getGrade());
    }
}
