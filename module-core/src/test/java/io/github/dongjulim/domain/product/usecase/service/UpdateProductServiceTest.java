package io.github.dongjulim.domain.product.usecase.service;

import io.github.dongjulim.domain.common.exception.ProductCategoryNotFoundException;
import io.github.dongjulim.domain.common.exception.ProductNotFoundException;
import io.github.dongjulim.domain.product.dto.UpdateProductRequest;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
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
class UpdateProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @InjectMocks
    private UpdateProductService updateProductService;

    @Test
    @DisplayName("updateProduct - 상품 정보가 정상적으로 변경된다")
    void updateProduct_shouldUpdateProduct() {
        Product product = Product.builder()
                .id(1L).userId(1L).name("사과").price(1000L).categoryId(5L).deleteCheck(false).build();
        ProductCategory newCategory = ProductCategory.builder().id(7L).name("과일").deleteCheck(false).build();

        UpdateProductRequest request = new UpdateProductRequest();
        ReflectionTestUtils.setField(request, "name", "배");
        ReflectionTestUtils.setField(request, "price", 2000L);
        ReflectionTestUtils.setField(request, "description", "달콤한 배");
        ReflectionTestUtils.setField(request, "categoryId", 7L);

        given(productRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(product));
        given(productCategoryRepository.findByIdAndDeleteCheckFalse(7L)).willReturn(Optional.of(newCategory));

        updateProductService.updateProduct(1L, request, null);

        assertThat(product.getName()).isEqualTo("배");
        assertThat(product.getPrice()).isEqualTo(2000L);
        assertThat(product.getDescription()).isEqualTo("달콤한 배");
        assertThat(product.getCategoryId()).isEqualTo(7L);
    }

    @Test
    @DisplayName("updateProduct - 존재하지 않는 상품이면 ProductNotFoundException을 던진다")
    void updateProduct_throwsProductNotFoundException_whenProductNotFound() {
        UpdateProductRequest request = new UpdateProductRequest();
        ReflectionTestUtils.setField(request, "name", "배");
        ReflectionTestUtils.setField(request, "price", 2000L);
        ReflectionTestUtils.setField(request, "categoryId", 7L);

        given(productRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> updateProductService.updateProduct(99L, request, null))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("updateProduct - 존재하지 않는 카테고리면 ProductCategoryNotFoundException을 던진다")
    void updateProduct_throwsProductCategoryNotFoundException_whenCategoryNotFound() {
        Product product = Product.builder()
                .id(1L).userId(1L).name("사과").price(1000L).categoryId(5L).deleteCheck(false).build();

        UpdateProductRequest request = new UpdateProductRequest();
        ReflectionTestUtils.setField(request, "name", "배");
        ReflectionTestUtils.setField(request, "price", 2000L);
        ReflectionTestUtils.setField(request, "categoryId", 99L);

        given(productRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(product));
        given(productCategoryRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> updateProductService.updateProduct(1L, request, null))
                .isInstanceOf(ProductCategoryNotFoundException.class);
    }
}
