package io.github.dongjulim.domain.user.usecase.service;

import io.github.dongjulim.domain.user.dto.SaveUserRequest;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.user.repository.UserRepository;
import io.github.dongjulim.domain.user.usecase.SaveUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SaveUserService implements SaveUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(SaveUserRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(request.getRole())
                .grade(request.getGrade())
                .build();
        userRepository.save(user);
    }
}
