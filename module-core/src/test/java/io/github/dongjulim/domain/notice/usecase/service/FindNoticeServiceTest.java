package io.github.dongjulim.domain.notice.usecase.service;

import io.github.dongjulim.domain.notice.dto.FindNoticeResponse;
import io.github.dongjulim.domain.notice.entity.Notice;
import io.github.dongjulim.domain.notice.repository.NoticeRepository;
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
class FindNoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private FindNoticeService findNoticeService;

    @Test
    @DisplayName("findNotice - 활성 공지사항 목록을 DTO로 변환하여 반환한다")
    void findNotice_returnsActiveNotices() {
        Notice notice1 = Notice.builder().title("제목1").content("내용1").deleteCheck(false).build();
        Notice notice2 = Notice.builder().title("제목2").content("내용2").deleteCheck(false).build();
        given(noticeRepository.findAllByDeleteCheckFalse()).willReturn(List.of(notice1, notice2));

        List<FindNoticeResponse> result = findNoticeService.findNotice();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("제목1");
        assertThat(result.get(1).getTitle()).isEqualTo("제목2");
    }

    @Test
    @DisplayName("findNotice - 활성 공지사항이 없으면 빈 목록을 반환한다")
    void findNotice_returnsEmptyList_whenNoActiveNotices() {
        given(noticeRepository.findAllByDeleteCheckFalse()).willReturn(List.of());

        List<FindNoticeResponse> result = findNoticeService.findNotice();

        assertThat(result).isEmpty();
    }
}
