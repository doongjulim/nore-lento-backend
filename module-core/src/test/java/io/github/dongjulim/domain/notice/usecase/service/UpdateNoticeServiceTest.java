package io.github.dongjulim.domain.notice.usecase.service;

import io.github.dongjulim.domain.common.exception.NoticeNotFoundException;
import io.github.dongjulim.domain.notice.dto.UpdateNoticeRequest;
import io.github.dongjulim.domain.notice.entity.Notice;
import io.github.dongjulim.domain.notice.enums.Category;
import io.github.dongjulim.domain.notice.repository.NoticeRepository;
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
class UpdateNoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private UpdateNoticeService updateNoticeService;

    @Test
    @DisplayName("updateNotice - 공지사항이 존재하면 정보가 변경된다")
    void updateNotice_shouldUpdateNotice_whenNoticeExists() {
        Notice notice = Notice.builder()
                .title("기존 제목").content("기존 내용").category(Category.NOTICE).deleteCheck(false)
                .build();
        UpdateNoticeRequest request = new UpdateNoticeRequest();
        ReflectionTestUtils.setField(request, "title", "새 제목");
        ReflectionTestUtils.setField(request, "content", "새 내용");
        ReflectionTestUtils.setField(request, "category", Category.NOTICE);
        given(noticeRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(notice));

        updateNoticeService.updateNotice(1L, request);

        assertThat(notice.getTitle()).isEqualTo("새 제목");
        assertThat(notice.getContent()).isEqualTo("새 내용");
        assertThat(notice.getCategory()).isEqualTo(Category.NOTICE);
    }

    @Test
    @DisplayName("updateNotice - 공지사항이 없으면 NoticeNotFoundException을 던진다")
    void updateNotice_throwsNoticeNotFoundException_whenNotFound() {
        UpdateNoticeRequest request = new UpdateNoticeRequest();
        given(noticeRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> updateNoticeService.updateNotice(1L, request))
                .isInstanceOf(NoticeNotFoundException.class);
    }
}
