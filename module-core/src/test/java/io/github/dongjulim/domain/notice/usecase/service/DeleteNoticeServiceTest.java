package io.github.dongjulim.domain.notice.usecase.service;

import io.github.dongjulim.domain.common.exception.NoticeNotFoundException;
import io.github.dongjulim.domain.notice.entity.Notice;
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
class DeleteNoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private DeleteNoticeService deleteNoticeService;

    @Test
    @DisplayName("deleteNotice - 활성 공지사항이 존재하면 soft delete 처리된다")
    void deleteNotice_shouldSoftDeleteNotice_whenNoticeExists() {
        Notice notice = Notice.builder()
                .title("제목").content("내용").deleteCheck(false)
                .build();
        given(noticeRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(notice));

        deleteNoticeService.deleteNotice(1L);

        assertThat(notice.getDeleteCheck()).isTrue();
    }

    @Test
    @DisplayName("deleteNotice - 공지사항이 없으면 NoticeNotFoundException을 던진다")
    void deleteNotice_throwsNoticeNotFoundException_whenNotFound() {
        given(noticeRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> deleteNoticeService.deleteNotice(1L))
                .isInstanceOf(NoticeNotFoundException.class);
    }
}
