package io.github.dongjulim.domain.user.usecase.service;

import io.github.dongjulim.domain.common.email.EmailSender;
import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.user.repository.UserRepository;
import io.github.dongjulim.domain.user.usecase.ResetPasswordUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ResetPasswordService implements ResetPasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;

    @Override
    public void resetPassword(String username) {
        User user = userRepository.findByUsernameAndDeleteCheck(username, false)
                .orElseThrow(UserNotFoundException::new);

        String temporaryPassword = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        user.updatePassword(passwordEncoder.encode(temporaryPassword));

        emailSender.send(
                username,
                "[노래렌토] 임시 비밀번호 발급",
                String.format("임시 비밀번호: %s\n로그인 후 반드시 비밀번호를 변경해 주세요.", temporaryPassword)
        );
    }
}
