package io.github.dongjulim.domain.product.usecase.service;

import io.github.dongjulim.domain.common.exception.ProductCategoryNotFoundException;
import io.github.dongjulim.domain.product.dto.SaveProductRequest;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.productCategory.entity.ProductCategory;
import io.github.dongjulim.domain.productCategory.repository.ProductCategoryRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SaveProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private SaveProductService saveProductService;

    @Test
    @DisplayName("saveProduct - 상품이 정상적으로 저장된다")
    void saveProduct_shouldSaveProduct() {
        User user = User.builder().id(1L).username("testuser").build();
        ProductCategory category = ProductCategory.builder().id(5L).name("식품").deleteCheck(false).build();

        SaveProductRequest request = new SaveProductRequest();
        ReflectionTestUtils.setField(request, "name", "사과");
        ReflectionTestUtils.setField(request, "price", 1000L);
        ReflectionTestUtils.setField(request, "description", "맛있는 사과");
        ReflectionTestUtils.setField(request, "categoryId", 5L);

        given(userLoader.load("testuser")).willReturn(user);
        given(productCategoryRepository.findByIdAndDeleteCheckFalse(5L)).willReturn(Optional.of(category));

        saveProductService.saveProduct(request, "testuser");

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        then(productRepository).should().save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("사과");
        assertThat(captor.getValue().getPrice()).isEqualTo(1000L);
        assertThat(captor.getValue().getDescription()).isEqualTo("맛있는 사과");
        assertThat(captor.getValue().getCategoryId()).isEqualTo(5L);
        assertThat(captor.getValue().getUserId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("saveProduct - 존재하지 않는 카테고리면 ProductCategoryNotFoundException을 던진다")
    void saveProduct_throwsProductCategoryNotFoundException_whenCategoryNotFound() {
        User user = User.builder().id(1L).username("testuser").build();

        SaveProductRequest request = new SaveProductRequest();
        ReflectionTestUtils.setField(request, "name", "사과");
        ReflectionTestUtils.setField(request, "price", 1000L);
        ReflectionTestUtils.setField(request, "categoryId", 99L);

        given(userLoader.load("testuser")).willReturn(user);
        given(productCategoryRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> saveProductService.saveProduct(request, "testuser"))
                .isInstanceOf(ProductCategoryNotFoundException.class);
    }
}
