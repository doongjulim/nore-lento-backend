package io.github.dongjulim.domain.user.usecase.service;

import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.user.dto.FindUserDetailResponse;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.user.repository.UserRepository;
import io.github.dongjulim.domain.user.usecase.FindUserDetailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindUserDetailService implements FindUserDetailUseCase {

    private final UserRepository userRepository;

    @Override
    public FindUserDetailResponse findUserDetail(Long id) {
        User user = userRepository.findByIdAndDeleteCheckFalse(id)
                .orElseThrow(UserNotFoundException::new);
        return FindUserDetailResponse.from(user);
    }
}
