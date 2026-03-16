package io.github.dongjulim.domain.notice.entity;

import io.github.dongjulim.domain.notice.enums.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeTest {

    private Notice createNotice() {
        return Notice.builder()
                .title("제목")
                .content("내용")
                .category(Category.NOTICE)
                .deleteCheck(false)
                .build();
    }

    @Test
    @DisplayName("updateNotice - 공지사항 정보가 정상적으로 변경된다")
    void updateNotice_shouldUpdateFields() {
        Notice notice = createNotice();

        notice.updateNotice("새 제목", "새 내용", Category.NOTICE);

        assertThat(notice.getTitle()).isEqualTo("새 제목");
        assertThat(notice.getContent()).isEqualTo("새 내용");
        assertThat(notice.getCategory()).isEqualTo(Category.NOTICE);
    }

    @Test
    @DisplayName("deleteNotice - deleteCheck가 true로 변경된다")
    void deleteNotice_shouldSetDeleteCheckTrue() {
        Notice notice = createNotice();

        notice.deleteNotice();

        assertThat(notice.getDeleteCheck()).isTrue();
    }
}
