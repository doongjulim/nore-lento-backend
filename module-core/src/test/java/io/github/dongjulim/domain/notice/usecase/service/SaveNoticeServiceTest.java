package io.github.dongjulim.domain.notice.usecase.service;

import io.github.dongjulim.domain.notice.dto.SaveNoticeRequest;
import io.github.dongjulim.domain.notice.entity.Notice;
import io.github.dongjulim.domain.notice.enums.Category;
import io.github.dongjulim.domain.notice.repository.NoticeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SaveNoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private SaveNoticeService saveNoticeService;

    @Test
    @DisplayName("saveNotice - 공지사항을 저장한다")
    void saveNotice_shouldSaveNotice() {
        SaveNoticeRequest request = new SaveNoticeRequest();
        ReflectionTestUtils.setField(request, "title", "공지 제목");
        ReflectionTestUtils.setField(request, "content", "공지 내용");
        ReflectionTestUtils.setField(request, "category", Category.NOTICE);

        saveNoticeService.saveNotice(request);

        ArgumentCaptor<Notice> captor = ArgumentCaptor.forClass(Notice.class);
        then(noticeRepository).should().save(captor.capture());
        Notice saved = captor.getValue();
        assertThat(saved.getTitle()).isEqualTo("공지 제목");
        assertThat(saved.getContent()).isEqualTo("공지 내용");
        assertThat(saved.getCategory()).isEqualTo(Category.NOTICE);
    }
}
