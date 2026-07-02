package io.github.dongjulim.domain.product.usecase.service;

import io.github.dongjulim.domain.common.exception.ProductNotFoundException;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class DeleteProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private DeleteProductService deleteProductService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L).userId(1L).name("사과").price(2000L).categoryId(1L).deleteCheck(false).build();
    }

    @Test
    @DisplayName("deleteProduct - 존재하는 상품을 삭제하면 deleteCheck가 true로 변경된다")
    void deleteProduct_shouldSetDeleteCheckTrue_whenProductExists() {
        given(productRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(product));

        deleteProductService.deleteProduct(1L);

        assertThat(product.getDeleteCheck()).isTrue();
        then(productRepository).should().findByIdAndDeleteCheckFalse(1L);
    }

    @Test
    @DisplayName("deleteProduct - 존재하지 않는 상품 ID로 삭제하면 ProductNotFoundException이 발생한다")
    void deleteProduct_shouldThrowProductNotFoundException_whenProductNotFound() {
        given(productRepository.findByIdAndDeleteCheckFalse(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> deleteProductService.deleteProduct(999L))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
