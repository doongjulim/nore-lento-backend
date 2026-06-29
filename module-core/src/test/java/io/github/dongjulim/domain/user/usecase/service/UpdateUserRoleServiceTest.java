package io.github.dongjulim.domain.user.usecase.service;

import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.user.dto.UpdateUserRoleRequest;
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
class UpdateUserRoleServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateUserRoleService updateUserRoleService;

    @Test
    @DisplayName("updateUserRole - 사용자 권한이 변경된다")
    void updateUserRole_shouldUpdateRole() {
        User user = User.builder().id(1L).username("user").name("홍길동")
                .role(Role.USER).grade(Grade.NORMAL).deleteCheck(false).build();
        UpdateUserRoleRequest request = new UpdateUserRoleRequest();
        ReflectionTestUtils.setField(request, "role", Role.ADMIN);

        given(userRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(user));

        updateUserRoleService.updateUserRole(1L, request);

        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("updateUserRole - 사용자가 없으면 UserNotFoundException을 던진다")
    void updateUserRole_throwsUserNotFoundException_whenUserNotFound() {
        UpdateUserRoleRequest request = new UpdateUserRoleRequest();
        ReflectionTestUtils.setField(request, "role", Role.ADMIN);

        given(userRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> updateUserRoleService.updateUserRole(99L, request))
                .isInstanceOf(UserNotFoundException.class);
    }
}
