package io.github.dongjulim.domain.user.usecase.service;

import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.user.dto.UpdateUserGradeRequest;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.user.repository.UserRepository;
import io.github.dongjulim.domain.user.usecase.UpdateUserGradeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateUserGradeService implements UpdateUserGradeUseCase {

    private final UserRepository userRepository;

    @Override
    public void updateUserGrade(Long userId, UpdateUserGradeRequest request) {
        User user = userRepository.findByIdAndDeleteCheckFalse(userId)
                .orElseThrow(UserNotFoundException::new);
        user.updateGrade(request.getGrade());
    }
}
