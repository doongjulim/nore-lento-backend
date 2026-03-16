package io.github.dongjulim.domain.user.usecase.service;

import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.user.repository.UserRepository;
import io.github.dongjulim.domain.user.usecase.DeleteUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteUserService implements DeleteUserUseCase {

    private final UserRepository userRepository;

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findByIdAndDeleteCheckFalse(id)
                .orElseThrow(UserNotFoundException::new);
        user.deleteUser();
    }
}
