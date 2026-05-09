package io.github.dongjulim.domain.productCategory.usecase.service;

import io.github.dongjulim.domain.common.exception.ProductCategoryNotFoundException;
import io.github.dongjulim.domain.productCategory.dto.UpdateProductCategoryRequest;
import io.github.dongjulim.domain.productCategory.entity.ProductCategory;
import io.github.dongjulim.domain.productCategory.repository.ProductCategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UpdateProductCategoryServiceTest {

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @InjectMocks
    private UpdateProductCategoryService updateProductCategoryService;

    @Test
    @DisplayName("updateProductCategory - 카테고리 이름이 정상적으로 변경된다")
    void updateProductCategory_shouldUpdateName() {
        ProductCategory category = ProductCategory.builder().id(1L).name("식품").deleteCheck(false).build();
        given(productCategoryRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(category));

        UpdateProductCategoryRequest request = new UpdateProductCategoryRequest();
        ReflectionTestUtils.setField(request, "name", "전자기기");

        updateProductCategoryService.updateProductCategory(1L, request);

        assertThat(category.getName()).isEqualTo("전자기기");
    }

    @Test
    @DisplayName("updateProductCategory - 존재하지 않는 카테고리면 ProductCategoryNotFoundException을 던진다")
    void updateProductCategory_throwsException_whenCategoryNotFound() {
        given(productCategoryRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        UpdateProductCategoryRequest request = new UpdateProductCategoryRequest();
        ReflectionTestUtils.setField(request, "name", "전자기기");

        assertThatThrownBy(() -> updateProductCategoryService.updateProductCategory(99L, request))
                .isInstanceOf(ProductCategoryNotFoundException.class);
    }
}
