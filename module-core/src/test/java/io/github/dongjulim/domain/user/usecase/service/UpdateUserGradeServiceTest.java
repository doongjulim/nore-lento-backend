package io.github.dongjulim.domain.user.usecase.service;

import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.user.dto.UpdateUserGradeRequest;
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
class UpdateUserGradeServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateUserGradeService updateUserGradeService;

    @Test
    @DisplayName("updateUserGrade - 사용자 등급이 변경된다")
    void updateUserGrade_shouldUpdateGrade() {
        User user = User.builder().id(1L).username("user").name("홍길동")
                .role(Role.USER).grade(Grade.NORMAL).deleteCheck(false).build();
        UpdateUserGradeRequest request = new UpdateUserGradeRequest();
        ReflectionTestUtils.setField(request, "grade", Grade.VIP);

        given(userRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(user));

        updateUserGradeService.updateUserGrade(1L, request);

        assertThat(user.getGrade()).isEqualTo(Grade.VIP);
    }

    @Test
    @DisplayName("updateUserGrade - 사용자가 없으면 UserNotFoundException을 던진다")
    void updateUserGrade_throwsUserNotFoundException_whenUserNotFound() {
        UpdateUserGradeRequest request = new UpdateUserGradeRequest();
        ReflectionTestUtils.setField(request, "grade", Grade.VIP);

        given(userRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> updateUserGradeService.updateUserGrade(99L, request))
                .isInstanceOf(UserNotFoundException.class);
    }
}
