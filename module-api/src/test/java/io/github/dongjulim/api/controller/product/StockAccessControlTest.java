package io.github.dongjulim.api.controller.product;

import io.github.dongjulim.domain.stock.usecase.UpdateStockUseCase;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UpdateStockController.class)
@Import(TestSecurityConfig.class)
@DisplayName("재고 접근 권한 테스트")
class StockAccessControlTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UpdateStockUseCase updateStockUseCase;

    // ── PATCH /api/v2/product/{id}/stock ─────────────────────────────────

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("재고 수정 - USER 역할은 403 반환")
    void updateStock_returns403_whenUserRole() throws Exception {
        mockMvc.perform(patch("/api/v2/product/1/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\": 100}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("재고 수정 - ADMIN 역할은 접근 허용")
    void updateStock_isAllowed_whenAdminRole() throws Exception {
        mockMvc.perform(patch("/api/v2/product/1/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\": 100}"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus())
                                .isNotEqualTo(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @WithMockUser(roles = "MASTER")
    @DisplayName("재고 수정 - MASTER 역할은 접근 허용")
    void updateStock_isAllowed_whenMasterRole() throws Exception {
        mockMvc.perform(patch("/api/v2/product/1/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\": 100}"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus())
                                .isNotEqualTo(HttpStatus.FORBIDDEN.value()));
    }
}
