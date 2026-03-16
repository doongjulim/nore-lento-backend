package io.github.dongjulim.domain.user.usecase.service;

import io.github.dongjulim.domain.user.dto.SaveUserRequest;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.user.enums.Grade;
import io.github.dongjulim.domain.user.enums.Role;
import io.github.dongjulim.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SaveUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SaveUserService saveUserService;

    @Test
    @DisplayName("saveUser - 비밀번호를 인코딩하여 사용자를 저장한다")
    void saveUser_shouldEncodePasswordAndSave() {
        SaveUserRequest request = new SaveUserRequest();
        ReflectionTestUtils.setField(request, "username", "testuser");
        ReflectionTestUtils.setField(request, "password", "rawPassword");
        ReflectionTestUtils.setField(request, "name", "Test User");
        ReflectionTestUtils.setField(request, "role", Role.USER);
        ReflectionTestUtils.setField(request, "grade", Grade.NORMAL);
        given(passwordEncoder.encode("rawPassword")).willReturn("encodedPassword");

        saveUserService.saveUser(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        then(userRepository).should().save(userCaptor.capture());
        User saved = userCaptor.getValue();
        assertThat(saved.getUsername()).isEqualTo("testuser");
        assertThat(saved.getPassword()).isEqualTo("encodedPassword");
        assertThat(saved.getName()).isEqualTo("Test User");
        assertThat(saved.getRole()).isEqualTo(Role.USER);
        assertThat(saved.getGrade()).isEqualTo(Grade.NORMAL);
    }
}
