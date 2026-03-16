package io.github.dongjulim.domain.user.usecase.service;

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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DeleteUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeleteUserService deleteUserService;

    @Test
    @DisplayName("deleteUser - 활성 사용자가 존재하면 soft delete 처리된다")
    void deleteUser_shouldSoftDeleteUser_whenUserExists() {
        User user = User.builder()
                .username("testuser").password("pwd").name("Test User")
                .role(Role.USER).grade(Grade.NORMAL).deleteCheck(false)
                .build();
        given(userRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(user));

        deleteUserService.deleteUser(1L);

        assertThat(user.getDeleteCheck()).isTrue();
    }

    @Test
    @DisplayName("deleteUser - 사용자가 없으면 UserNotFoundException을 던진다")
    void deleteUser_throwsUserNotFoundException_whenUserNotFound() {
        given(userRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> deleteUserService.deleteUser(1L))
                .isInstanceOf(UserNotFoundException.class);
    }
}
