package io.github.dongjulim.domain.user.usecase.service;

import io.github.dongjulim.domain.user.dto.FindUserResponse;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FindUserService findUserService;

    @Test
    @DisplayName("findUser - 활성 사용자 목록을 DTO로 변환하여 반환한다")
    void findUser_returnsActiveUsers() {
        User user1 = User.builder().username("user1").password("pwd").name("User One")
                .role(Role.USER).grade(Grade.NORMAL).deleteCheck(false).build();
        User user2 = User.builder().username("user2").password("pwd").name("User Two")
                .role(Role.ADMIN).grade(Grade.VIP).deleteCheck(false).build();
        given(userRepository.findAllByDeleteCheckFalse()).willReturn(List.of(user1, user2));

        List<FindUserResponse> result = findUserService.findUser();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUsername()).isEqualTo("user1");
        assertThat(result.get(1).getUsername()).isEqualTo("user2");
    }

    @Test
    @DisplayName("findUser - 활성 사용자가 없으면 빈 목록을 반환한다")
    void findUser_returnsEmptyList_whenNoActiveUsers() {
        given(userRepository.findAllByDeleteCheckFalse()).willReturn(List.of());

        List<FindUserResponse> result = findUserService.findUser();

        assertThat(result).isEmpty();
    }
}
