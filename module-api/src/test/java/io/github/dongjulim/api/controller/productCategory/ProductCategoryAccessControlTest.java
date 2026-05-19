package io.github.dongjulim.api.controller.productCategory;

import io.github.dongjulim.domain.productCategory.usecase.DeleteProductCategoryUseCase;
import io.github.dongjulim.domain.productCategory.usecase.SaveProductCategoryUseCase;
import io.github.dongjulim.domain.productCategory.usecase.UpdateProductCategoryUseCase;
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

@WebMvcTest({SaveProductCategoryController.class, UpdateProductCategoryController.class, DeleteProductCategoryController.class})
@Import(TestSecurityConfig.class)
@DisplayName("상품 카테고리 접근 권한 테스트")
class ProductCategoryAccessControlTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private SaveProductCategoryUseCase saveProductCategoryUseCase;
    @MockBean private UpdateProductCategoryUseCase updateProductCategoryUseCase;
    @MockBean private DeleteProductCategoryUseCase deleteProductCategoryUseCase;

    // ── POST /api/v2/product/categories ──────────────────────────────────

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("카테고리 생성 - USER 역할은 403 반환")
    void saveProductCategory_returns403_whenUserRole() throws Exception {
        mockMvc.perform(post("/api/v2/product/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("카테고리 생성 - ADMIN 역할은 접근 허용")
    void saveProductCategory_isAllowed_whenAdminRole() throws Exception {
        mockMvc.perform(post("/api/v2/product/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus())
                                .isNotEqualTo(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    @WithMockUser(roles = "MASTER")
    @DisplayName("카테고리 생성 - MASTER 역할은 접근 허용")
    void saveProductCategory_isAllowed_whenMasterRole() throws Exception {
        mockMvc.perform(post("/api/v2/product/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus())
                                .isNotEqualTo(HttpStatus.FORBIDDEN.value()));
    }

    // ── PUT /api/v2/product/categories/{id} ──────────────────────────────

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("카테고리 수정 - USER 역할은 403 반환")
    void updateProductCategory_returns403_whenUserRole() throws Exception {
        mockMvc.perform(put("/api/v2/product/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("카테고리 수정 - ADMIN 역할은 접근 허용")
    void updateProductCategory_isAllowed_whenAdminRole() throws Exception {
        mockMvc.perform(put("/api/v2/product/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus())
                                .isNotEqualTo(HttpStatus.FORBIDDEN.value()));
    }

    // ── DELETE /api/v2/product/categories/{id} ────────────────────────────

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("카테고리 삭제 - USER 역할은 403 반환")
    void deleteProductCategory_returns403_whenUserRole() throws Exception {
        mockMvc.perform(delete("/api/v2/product/categories/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("카테고리 삭제 - ADMIN 역할은 접근 허용")
    void deleteProductCategory_isAllowed_whenAdminRole() throws Exception {
        mockMvc.perform(delete("/api/v2/product/categories/1"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus())
                                .isNotEqualTo(HttpStatus.FORBIDDEN.value()));
    }
}
