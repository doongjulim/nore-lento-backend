package io.github.dongjulim.domain.productCategory.usecase.service;

import io.github.dongjulim.domain.productCategory.dto.FindProductCategoryResponse;
import io.github.dongjulim.domain.productCategory.entity.ProductCategory;
import io.github.dongjulim.domain.productCategory.repository.ProductCategoryRepository;
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
class FindProductCategoryServiceTest {

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @InjectMocks
    private FindProductCategoryService findProductCategoryService;

    @Test
    @DisplayName("findProductCategories - 삭제되지 않은 카테고리 목록을 반환한다")
    void findProductCategories_shouldReturnActiveCategories() {
        ProductCategory category1 = ProductCategory.builder().id(1L).name("식품").deleteCheck(false).build();
        ProductCategory category2 = ProductCategory.builder().id(2L).name("의류").deleteCheck(false).build();
        given(productCategoryRepository.findAllByDeleteCheckFalse()).willReturn(List.of(category1, category2));

        List<FindProductCategoryResponse> result = findProductCategoryService.findProductCategories();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isEqualTo("식품");
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getName()).isEqualTo("의류");
    }

    @Test
    @DisplayName("findProductCategories - 카테고리가 없으면 빈 목록을 반환한다")
    void findProductCategories_shouldReturnEmptyList_whenNoneExist() {
        given(productCategoryRepository.findAllByDeleteCheckFalse()).willReturn(List.of());

        List<FindProductCategoryResponse> result = findProductCategoryService.findProductCategories();

        assertThat(result).isEmpty();
    }
}
