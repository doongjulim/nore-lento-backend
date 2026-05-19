package io.github.dongjulim.api.controller.notification;

import io.github.dongjulim.domain.notification.usecase.SaveNotificationUseCase;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SaveNotificationController.class)
@Import(TestSecurityConfig.class)
@DisplayName("알림 접근 권한 테스트")
class NotificationAccessControlTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private SaveNotificationUseCase saveNotificationUseCase;

    // ── POST /api/v2/notifications ────────────────────────────────────────

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("알림 생성 - USER 역할은 403 반환")
    void saveNotification_returns403_whenUserRole() throws Exception {
        mockMvc.perform(post("/api/v2/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("알림 생성 - ADMIN 역할은 접근 허용")
    void saveNotification_isAllowed_whenAdminRole() throws Exception {
        mockMvc.perform(post("/api/v2/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus())
                                .isNotEqualTo(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @WithMockUser(roles = "MASTER")
    @DisplayName("알림 생성 - MASTER 역할은 접근 허용")
    void saveNotification_isAllowed_whenMasterRole() throws Exception {
        mockMvc.perform(post("/api/v2/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus())
                                .isNotEqualTo(HttpStatus.FORBIDDEN.value()));
    }
}
