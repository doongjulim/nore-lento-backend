package io.github.dongjulim.domain.user.usecase.service;

import io.github.dongjulim.domain.common.exception.InvalidPasswordException;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UpdatePasswordServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UpdatePasswordService updatePasswordService;

    @Test
    @DisplayName("updatePassword - 현재 비밀번호가 일치하면 새 비밀번호로 변경된다")
    void updatePassword_shouldUpdatePassword_whenCurrentPasswordMatches() {
        User user = User.builder().id(1L).username("testuser").password("encodedOldPwd")
                .name("홍길동").role(Role.USER).grade(Grade.NORMAL).deleteCheck(false).build();

        given(userRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(user));
        given(passwordEncoder.matches("oldPwd", "encodedOldPwd")).willReturn(true);
        given(passwordEncoder.encode("newPwd")).willReturn("encodedNewPwd");

        updatePasswordService.updatePassword(1L, "oldPwd", "newPwd");

        assertThat(user.getPassword()).isEqualTo("encodedNewPwd");
    }

    @Test
    @DisplayName("updatePassword - 현재 비밀번호가 일치하지 않으면 InvalidPasswordException을 던진다")
    void updatePassword_throwsInvalidPasswordException_whenCurrentPasswordNotMatch() {
        User user = User.builder().id(1L).username("testuser").password("encodedOldPwd")
                .name("홍길동").role(Role.USER).grade(Grade.NORMAL).deleteCheck(false).build();

        given(userRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(user));
        given(passwordEncoder.matches("wrongPwd", "encodedOldPwd")).willReturn(false);

        assertThatThrownBy(() -> updatePasswordService.updatePassword(1L, "wrongPwd", "newPwd"))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    @DisplayName("updatePassword - 존재하지 않는 사용자면 UserNotFoundException을 던진다")
    void updatePassword_throwsUserNotFoundException_whenUserNotFound() {
        given(userRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> updatePasswordService.updatePassword(99L, "oldPwd", "newPwd"))
                .isInstanceOf(UserNotFoundException.class);
    }
}
