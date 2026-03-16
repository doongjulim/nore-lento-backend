package io.github.dongjulim.domain.notice.usecase.service;

import io.github.dongjulim.domain.common.exception.NoticeNotFoundException;
import io.github.dongjulim.domain.notice.dto.FindNoticeDetailResponse;
import io.github.dongjulim.domain.notice.entity.Notice;
import io.github.dongjulim.domain.notice.repository.NoticeLikeRepository;
import io.github.dongjulim.domain.notice.repository.NoticeRepository;
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
class FindNoticeDetailServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private NoticeLikeRepository noticeLikeRepository;

    @InjectMocks
    private FindNoticeDetailService findNoticeDetailService;

    @Test
    @DisplayName("findNoticeDetail - 활성 공지사항이 존재하면 상세 정보를 반환한다")
    void findNoticeDetail_returnsDetailResponse_whenNoticeExists() {
        Notice notice = Notice.builder()
                .title("제목").content("내용").deleteCheck(false)
                .build();
        given(noticeRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(notice));
        given(noticeLikeRepository.countByNoticeId(1L)).willReturn(5L);

        FindNoticeDetailResponse result = findNoticeDetailService.findNoticeDetail(1L);

        assertThat(result.getTitle()).isEqualTo("제목");
        assertThat(result.getContent()).isEqualTo("내용");
        assertThat(result.getLikeCount()).isEqualTo(5L);
    }

    @Test
    @DisplayName("findNoticeDetail - 공지사항이 없으면 NoticeNotFoundException을 던진다")
    void findNoticeDetail_throwsNoticeNotFoundException_whenNotFound() {
        given(noticeRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> findNoticeDetailService.findNoticeDetail(1L))
                .isInstanceOf(NoticeNotFoundException.class);
    }
}
