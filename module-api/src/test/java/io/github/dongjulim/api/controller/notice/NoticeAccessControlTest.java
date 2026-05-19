package io.github.dongjulim.api.controller.notice;

import io.github.dongjulim.domain.notice.usecase.DeleteNoticeUseCase;
import io.github.dongjulim.domain.notice.usecase.SaveNoticeUseCase;
import io.github.dongjulim.domain.notice.usecase.UpdateNoticeUseCase;
import io.github.dongjulim.security.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({SaveNoticeController.class, UpdateNoticeController.class, DeleteNoticeController.class})
@Import(TestSecurityConfig.class)
@DisplayName("공지사항 접근 권한 테스트")
class NoticeAccessControlTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private SaveNoticeUseCase saveNoticeUseCase;
    @MockBean private UpdateNoticeUseCase updateNoticeUseCase;
    @MockBean private DeleteNoticeUseCase deleteNoticeUseCase;

    // ── POST /api/v2/notice ───────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("공지 작성 - USER 역할은 403 반환")
    void saveNotice_returns403_whenUserRole() throws Exception {
        mockMvc.perform(post("/api/v2/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("공지 작성 - ADMIN 역할은 접근 허용")
    void saveNotice_isAllowed_whenAdminRole() throws Exception {
        mockMvc.perform(post("/api/v2/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus())
                                .isNotEqualTo(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @WithMockUser(roles = "MASTER")
    @DisplayName("공지 작성 - MASTER 역할은 접근 허용")
    void saveNotice_isAllowed_whenMasterRole() throws Exception {
        mockMvc.perform(post("/api/v2/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus())
                                .isNotEqualTo(HttpStatus.FORBIDDEN.value()));
    }

    // ── PUT /api/v2/notice/{id} ───────────────────────────────────────────

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("공지 수정 - USER 역할은 403 반환")
    void updateNotice_returns403_whenUserRole() throws Exception {
        mockMvc.perform(put("/api/v2/notice/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("공지 수정 - ADMIN 역할은 접근 허용")
    void updateNotice_isAllowed_whenAdminRole() throws Exception {
        mockMvc.perform(put("/api/v2/notice/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus())
                                .isNotEqualTo(HttpStatus.FORBIDDEN.value()));
    }

    // ── DELETE /api/v2/notice/{id} ────────────────────────────────────────

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("공지 삭제 - USER 역할은 403 반환")
    void deleteNotice_returns403_whenUserRole() throws Exception {
        mockMvc.perform(delete("/api/v2/notice/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("공지 삭제 - ADMIN 역할은 접근 허용")
    void deleteNotice_isAllowed_whenAdminRole() throws Exception {
        mockMvc.perform(delete("/api/v2/notice/1"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus())
                                .isNotEqualTo(HttpStatus.FORBIDDEN.value()));
    }
}
