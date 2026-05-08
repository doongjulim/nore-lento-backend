package io.github.dongjulim.domain.notice.usecase.service;

import io.github.dongjulim.domain.common.exception.NoticeLikeNotFoundException;
import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.notice.entity.Notice;
import io.github.dongjulim.domain.notice.entity.NoticeLike;
import io.github.dongjulim.domain.notice.repository.NoticeLikeRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.user.enums.Grade;
import io.github.dongjulim.domain.user.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UnlikeNoticeServiceTest {

    @Mock
    private UserLoader userLoader;

    @Mock
    private NoticeLikeRepository noticeLikeRepository;

    @InjectMocks
    private UnlikeNoticeService unlikeNoticeService;

    private User createUser() {
        return User.builder()
                .username("testuser").password("pwd").name("테스트")
                .role(Role.USER).grade(Grade.NORMAL).deleteCheck(false)
                .build();
    }

    @Test
    @DisplayName("unlikeNotice - 좋아요가 존재하면 정상적으로 삭제한다")
    void unlikeNotice_shouldDeleteNoticeLike() {
        User user = createUser();
        Notice notice = Notice.builder().title("제목").content("내용").deleteCheck(false).build();
        NoticeLike noticeLike = NoticeLike.builder().notice(notice).user(user).build();
        given(userLoader.load("testuser")).willReturn(user);
        given(noticeLikeRepository.findByNoticeIdAndUserId(1L, user.getId())).willReturn(Optional.of(noticeLike));

        unlikeNoticeService.unlikeNotice(1L, "testuser");

        then(noticeLikeRepository).should().delete(noticeLike);
    }

    @Test
    @DisplayName("unlikeNotice - 좋아요가 없으면 NoticeLikeNotFoundException을 던진다")
    void unlikeNotice_throwsNoticeLikeNotFoundException_whenNotFound() {
        User user = createUser();
        given(userLoader.load("testuser")).willReturn(user);
        given(noticeLikeRepository.findByNoticeIdAndUserId(1L, user.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> unlikeNoticeService.unlikeNotice(1L, "testuser"))
                .isInstanceOf(NoticeLikeNotFoundException.class);
    }

    @Test
    @DisplayName("unlikeNotice - 유저가 없으면 UserNotFoundException을 던진다")
    void unlikeNotice_throwsUserNotFoundException_whenUserNotFound() {
        given(userLoader.load("testuser")).willThrow(new UserNotFoundException());

        assertThatThrownBy(() -> unlikeNoticeService.unlikeNotice(1L, "testuser"))
                .isInstanceOf(UserNotFoundException.class);
    }
}
