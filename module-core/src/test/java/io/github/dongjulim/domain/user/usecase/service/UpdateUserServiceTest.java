package io.github.dongjulim.domain.user.usecase.service;

import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.user.dto.UpdateUserRequest;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UpdateUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateUserService updateUserService;

    @Test
    @DisplayName("updateUser - 사용자가 존재하면 정보가 변경된다")
    void updateUser_shouldUpdateUser_whenUserExists() {
        User user = User.builder()
                .username("olduser").password("pwd").name("Old Name")
                .role(Role.USER).grade(Grade.NORMAL).deleteCheck(false)
                .build();
        UpdateUserRequest request = new UpdateUserRequest();
        ReflectionTestUtils.setField(request, "username", "newuser");
        ReflectionTestUtils.setField(request, "name", "New Name");
        ReflectionTestUtils.setField(request, "role", Role.ADMIN);
        ReflectionTestUtils.setField(request, "grade", Grade.VIP);
        given(userRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(user));

        updateUserService.updateUser(1L, request);

        assertThat(user.getUsername()).isEqualTo("newuser");
        assertThat(user.getName()).isEqualTo("New Name");
        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
        assertThat(user.getGrade()).isEqualTo(Grade.VIP);
    }

    @Test
    @DisplayName("updateUser - 사용자가 없으면 UserNotFoundException을 던진다")
    void updateUser_throwsUserNotFoundException_whenUserNotFound() {
        UpdateUserRequest request = new UpdateUserRequest();
        given(userRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> updateUserService.updateUser(1L, request))
                .isInstanceOf(UserNotFoundException.class);
    }
}
