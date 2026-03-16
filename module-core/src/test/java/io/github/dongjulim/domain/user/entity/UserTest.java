package io.github.dongjulim.domain.user.entity;

import io.github.dongjulim.domain.user.enums.Grade;
import io.github.dongjulim.domain.user.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User createUser() {
        return User.builder()
                .username("testuser")
                .password("encoded_password")
                .name("Test User")
                .role(Role.USER)
                .grade(Grade.NORMAL)
                .deleteCheck(false)
                .build();
    }

    @Test
    @DisplayName("updateUser - 사용자 정보가 정상적으로 변경된다")
    void updateUser_shouldUpdateFields() {
        User user = createUser();

        user.updateUser("newuser", "New Name", Role.ADMIN, Grade.VIP);

        assertThat(user.getUsername()).isEqualTo("newuser");
        assertThat(user.getName()).isEqualTo("New Name");
        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
        assertThat(user.getGrade()).isEqualTo(Grade.VIP);
    }

    @Test
    @DisplayName("updateUser - 변경되지 않아야 할 필드는 그대로다")
    void updateUser_shouldNotChangePasswordOrDeleteCheck() {
        User user = createUser();

        user.updateUser("newuser", "New Name", Role.ADMIN, Grade.VIP);

        assertThat(user.getPassword()).isEqualTo("encoded_password");
        assertThat(user.getDeleteCheck()).isFalse();
    }

    @Test
    @DisplayName("updatePassword - 비밀번호가 정상적으로 변경된다")
    void updatePassword_shouldUpdatePassword() {
        User user = createUser();

        user.updatePassword("new_encoded_password");

        assertThat(user.getPassword()).isEqualTo("new_encoded_password");
    }

    @Test
    @DisplayName("deleteUser - deleteCheck가 true로 변경된다")
    void deleteUser_shouldSetDeleteCheckTrue() {
        User user = createUser();

        user.deleteUser();

        assertThat(user.getDeleteCheck()).isTrue();
    }
}
