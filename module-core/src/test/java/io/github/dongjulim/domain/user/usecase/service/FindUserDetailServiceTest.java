package io.github.dongjulim.domain.user.usecase.service;

import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.user.dto.FindUserDetailResponse;
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
class FindUserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FindUserDetailService findUserDetailService;

    @Test
    @DisplayName("findUserDetail - 활성 사용자가 존재하면 상세 정보를 반환한다")
    void findUserDetail_returnsDetailResponse_whenUserExists() {
        User user = User.builder()
                .username("testuser").password("pwd").name("Test User")
                .role(Role.USER).grade(Grade.NORMAL).deleteCheck(false)
                .build();
        given(userRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(user));

        FindUserDetailResponse result = findUserDetailService.findUserDetail(1L);

        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getName()).isEqualTo("Test User");
        assertThat(result.getRole()).isEqualTo(Role.USER);
        assertThat(result.getGrade()).isEqualTo(Grade.NORMAL);
    }

    @Test
    @DisplayName("findUserDetail - 사용자가 없으면 UserNotFoundException을 던진다")
    void findUserDetail_throwsUserNotFoundException_whenUserNotFound() {
        given(userRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> findUserDetailService.findUserDetail(1L))
                .isInstanceOf(UserNotFoundException.class);
    }
}
