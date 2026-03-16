package io.github.dongjulim.domain.notice.usecase.service;

import io.github.dongjulim.domain.common.exception.AlreadyLikedException;
import io.github.dongjulim.domain.common.exception.NoticeNotFoundException;
import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.notice.entity.Notice;
import io.github.dongjulim.domain.notice.entity.NoticeLike;
import io.github.dongjulim.domain.notice.repository.NoticeLikeRepository;
import io.github.dongjulim.domain.notice.repository.NoticeRepository;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class LikeNoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NoticeLikeRepository noticeLikeRepository;

    @InjectMocks
    private LikeNoticeService likeNoticeService;

    private Notice createNotice() {
        return Notice.builder().title("제목").content("내용").deleteCheck(false).build();
    }

    private User createUser() {
        return User.builder()
                .username("testuser").password("pwd").name("테스트")
                .role(Role.USER).grade(Grade.NORMAL).deleteCheck(false)
                .build();
    }

    @Test
    @DisplayName("likeNotice - 좋아요를 정상적으로 저장한다")
    void likeNotice_shouldSaveNoticeLike() {
        Notice notice = createNotice();
        User user = createUser();
        given(noticeRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(notice));
        given(userRepository.findByUsernameAndDeleteCheck("testuser", false)).willReturn(Optional.of(user));
        given(noticeLikeRepository.existsByNoticeIdAndUserId(1L, user.getId())).willReturn(false);

        likeNoticeService.likeNotice(1L, "testuser");

        ArgumentCaptor<NoticeLike> captor = ArgumentCaptor.forClass(NoticeLike.class);
        then(noticeLikeRepository).should().save(captor.capture());
        assertThat(captor.getValue().getNotice()).isEqualTo(notice);
        assertThat(captor.getValue().getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("likeNotice - 이미 좋아요를 누른 경우 AlreadyLikedException을 던진다")
    void likeNotice_throwsAlreadyLikedException_whenAlreadyLiked() {
        Notice notice = createNotice();
        User user = createUser();
        given(noticeRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(notice));
        given(userRepository.findByUsernameAndDeleteCheck("testuser", false)).willReturn(Optional.of(user));
        given(noticeLikeRepository.existsByNoticeIdAndUserId(1L, user.getId())).willReturn(true);

        assertThatThrownBy(() -> likeNoticeService.likeNotice(1L, "testuser"))
                .isInstanceOf(AlreadyLikedException.class);
    }

    @Test
    @DisplayName("likeNotice - 공지사항이 없으면 NoticeNotFoundException을 던진다")
    void likeNotice_throwsNoticeNotFoundException_whenNoticeNotFound() {
        given(noticeRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> likeNoticeService.likeNotice(1L, "testuser"))
                .isInstanceOf(NoticeNotFoundException.class);
    }

    @Test
    @DisplayName("likeNotice - 유저가 없으면 UserNotFoundException을 던진다")
    void likeNotice_throwsUserNotFoundException_whenUserNotFound() {
        given(noticeRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(createNotice()));
        given(userRepository.findByUsernameAndDeleteCheck("testuser", false)).willReturn(Optional.empty());

        assertThatThrownBy(() -> likeNoticeService.likeNotice(1L, "testuser"))
                .isInstanceOf(UserNotFoundException.class);
    }
}
