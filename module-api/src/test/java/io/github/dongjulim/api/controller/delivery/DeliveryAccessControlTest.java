package io.github.dongjulim.api.controller.delivery;

import io.github.dongjulim.domain.delivery.usecase.CreateDeliveryUseCase;
import io.github.dongjulim.domain.delivery.usecase.ReturnDeliveryUseCase;
import io.github.dongjulim.domain.delivery.usecase.UpdateDeliveryStatusUseCase;
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

@WebMvcTest({CreateDeliveryController.class, UpdateDeliveryStatusController.class, ReturnDeliveryController.class})
@Import(TestSecurityConfig.class)
@DisplayName("배송 접근 권한 테스트")
class DeliveryAccessControlTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private CreateDeliveryUseCase createDeliveryUseCase;
    @MockBean private UpdateDeliveryStatusUseCase updateDeliveryStatusUseCase;
    @MockBean private ReturnDeliveryUseCase returnDeliveryUseCase;

    // ── POST /api/v2/deliveries ──────────────────────────────────────────

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("배송 생성 - USER 역할은 403 반환")
    void createDelivery_returns403_whenUserRole() throws Exception {
        mockMvc.perform(post("/api/v2/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("배송 생성 - ADMIN 역할은 접근 허용")
    void createDelivery_isAllowed_whenAdminRole() throws Exception {
        mockMvc.perform(post("/api/v2/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus())
                                .isNotEqualTo(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @WithMockUser(roles = "MASTER")
    @DisplayName("배송 생성 - MASTER 역할은 접근 허용")
    void createDelivery_isAllowed_whenMasterRole() throws Exception {
        mockMvc.perform(post("/api/v2/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus())
                                .isNotEqualTo(HttpStatus.FORBIDDEN.value()));
    }

    // ── PATCH /api/v2/deliveries/{id}/status ─────────────────────────────

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("배송 상태 변경 - USER 역할은 403 반환")
    void updateDeliveryStatus_returns403_whenUserRole() throws Exception {
        mockMvc.perform(patch("/api/v2/deliveries/1/status"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("배송 상태 변경 - ADMIN 역할은 접근 허용")
    void updateDeliveryStatus_isAllowed_whenAdminRole() throws Exception {
        mockMvc.perform(patch("/api/v2/deliveries/1/status"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus())
                                .isNotEqualTo(HttpStatus.FORBIDDEN.value()));
    }

    // ── DELETE /api/v2/deliveries/{id}/return ────────────────────────────

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("반품 처리 - USER 역할은 403 반환")
    void returnDelivery_returns403_whenUserRole() throws Exception {
        mockMvc.perform(delete("/api/v2/deliveries/1/return"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("반품 처리 - ADMIN 역할은 접근 허용")
    void returnDelivery_isAllowed_whenAdminRole() throws Exception {
        mockMvc.perform(delete("/api/v2/deliveries/1/return"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus())
                                .isNotEqualTo(HttpStatus.FORBIDDEN.value()));
    }
}
