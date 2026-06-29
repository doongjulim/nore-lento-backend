package io.github.dongjulim.domain.user.usecase.service;

import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.user.dto.UpdateUserRoleRequest;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.user.repository.UserRepository;
import io.github.dongjulim.domain.user.usecase.UpdateUserRoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateUserRoleService implements UpdateUserRoleUseCase {

    private final UserRepository userRepository;

    @Override
    public void updateUserRole(Long userId, UpdateUserRoleRequest request) {
        User user = userRepository.findByIdAndDeleteCheckFalse(userId)
                .orElseThrow(UserNotFoundException::new);
        user.updateRole(request.getRole());
    }
}
