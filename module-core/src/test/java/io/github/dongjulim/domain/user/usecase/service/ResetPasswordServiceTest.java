package io.github.dongjulim.domain.user.usecase.service;

import io.github.dongjulim.domain.common.email.EmailSender;
import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.user.enums.Grade;
import io.github.dongjulim.domain.user.enums.Role;
import io.github.dongjulim.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ResetPasswordServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private ResetPasswordService resetPasswordService;

    @Test
    @DisplayName("resetPassword - 유효한 username으로 임시 비밀번호를 발급하고 이메일을 발송한다")
    void resetPassword_shouldIssueTemporaryPasswordAndSendEmail_whenUsernameExists() {
        User user = User.builder()
                .id(1L).username("user@example.com").password("encodedOldPwd")
                .name("홍길동").role(Role.USER).grade(Grade.NORMAL).deleteCheck(false)
                .build();

        given(userRepository.findByUsernameAndDeleteCheck("user@example.com", false))
                .willReturn(Optional.of(user));
        given(passwordEncoder.encode(anyString())).willReturn("encodedTempPwd");

        resetPasswordService.resetPassword("user@example.com");

        assertThat(user.getPassword()).isEqualTo("encodedTempPwd");
        then(emailSender).should().send(eq("user@example.com"), anyString(), anyString());
    }

    @Test
    @DisplayName("resetPassword - 존재하지 않거나 삭제된 username이면 UserNotFoundException을 던진다")
    void resetPassword_throwsUserNotFoundException_whenUsernameNotFound() {
        given(userRepository.findByUsernameAndDeleteCheck("unknown@example.com", false))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> resetPasswordService.resetPassword("unknown@example.com"))
                .isInstanceOf(UserNotFoundException.class);
    }
}
