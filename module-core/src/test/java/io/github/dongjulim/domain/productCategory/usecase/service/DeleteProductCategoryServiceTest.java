package io.github.dongjulim.domain.productCategory.usecase.service;

import io.github.dongjulim.domain.common.exception.ProductCategoryNotFoundException;
import io.github.dongjulim.domain.productCategory.entity.ProductCategory;
import io.github.dongjulim.domain.productCategory.repository.ProductCategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DeleteProductCategoryServiceTest {

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @InjectMocks
    private DeleteProductCategoryService deleteProductCategoryService;

    @Test
    @DisplayName("deleteProductCategory - deleteCheck가 true로 변경된다")
    void deleteProductCategory_shouldSetDeleteCheckTrue() {
        ProductCategory category = ProductCategory.builder().id(1L).name("식품").deleteCheck(false).build();
        given(productCategoryRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(category));

        deleteProductCategoryService.deleteProductCategory(1L);

        assertThat(category.getDeleteCheck()).isTrue();
    }

    @Test
    @DisplayName("deleteProductCategory - 존재하지 않는 카테고리면 ProductCategoryNotFoundException을 던진다")
    void deleteProductCategory_throwsException_whenCategoryNotFound() {
        given(productCategoryRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> deleteProductCategoryService.deleteProductCategory(99L))
                .isInstanceOf(ProductCategoryNotFoundException.class);
    }
}
