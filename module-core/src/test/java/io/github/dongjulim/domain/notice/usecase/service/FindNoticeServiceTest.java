package io.github.dongjulim.domain.notice.usecase.service;

import io.github.dongjulim.domain.notice.dto.FindNoticeRequest;
import io.github.dongjulim.domain.notice.dto.FindNoticeResponse;
import io.github.dongjulim.domain.notice.entity.Notice;
import io.github.dongjulim.domain.notice.enums.Category;
import io.github.dongjulim.domain.notice.repository.NoticeRepository;
import io.github.dongjulim.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindNoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private FindNoticeService findNoticeService;

    private Notice createNotice(String title, String content, Category category) {
        User user = User.builder().id(1L).name("작성자").build();
        Notice notice = Notice.builder().title(title).content(content).category(category).deleteCheck(false).build();
        ReflectionTestUtils.setField(notice, "user", user);
        return notice;
    }

    @Test
    @DisplayName("findNotice - 검색 조건에 맞는 공지사항 목록을 페이징하여 반환한다")
    void findNotice_returnsPagedNotices() {
        Notice notice1 = createNotice("제목1", "내용1", Category.NOTICE);
        Notice notice2 = createNotice("제목2", "내용2", Category.NOTICE);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Notice> noticePage = new PageImpl<>(List.of(notice1, notice2), pageable, 2);

        FindNoticeRequest request = new FindNoticeRequest();
        given(noticeRepository.findAllBySearchCondition(null, null, pageable)).willReturn(noticePage);

        Page<FindNoticeResponse> result = findNoticeService.findNotice(request, pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("제목1");
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("findNotice - category 필터로 검색하면 해당 카테고리만 반환한다")
    void findNotice_filtersByCategory() {
        Notice notice = createNotice("공지", "내용", Category.QA);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Notice> noticePage = new PageImpl<>(List.of(notice), pageable, 1);

        FindNoticeRequest request = new FindNoticeRequest();
        ReflectionTestUtils.setField(request, "category", Category.QA);
        given(noticeRepository.findAllBySearchCondition(Category.QA, null, pageable)).willReturn(noticePage);

        Page<FindNoticeResponse> result = findNoticeService.findNotice(request, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCategory()).isEqualTo(Category.QA);
    }

    @Test
    @DisplayName("findNotice - keyword 필터로 검색하면 제목에 포함된 공지만 반환한다")
    void findNotice_filtersByKeyword() {
        Notice notice = createNotice("긴급 공지", "내용", Category.NOTICE);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Notice> noticePage = new PageImpl<>(List.of(notice), pageable, 1);

        FindNoticeRequest request = new FindNoticeRequest();
        ReflectionTestUtils.setField(request, "keyword", "긴급");
        given(noticeRepository.findAllBySearchCondition(null, "긴급", pageable)).willReturn(noticePage);

        Page<FindNoticeResponse> result = findNoticeService.findNotice(request, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("긴급 공지");
    }

    @Test
    @DisplayName("findNotice - 검색 결과가 없으면 빈 페이지를 반환한다")
    void findNotice_returnsEmptyPage_whenNoResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Notice> emptyPage = Page.empty(pageable);

        FindNoticeRequest request = new FindNoticeRequest();
        given(noticeRepository.findAllBySearchCondition(null, null, pageable)).willReturn(emptyPage);

        Page<FindNoticeResponse> result = findNoticeService.findNotice(request, pageable);

        assertThat(result.getContent()).isEmpty();
    }
}
