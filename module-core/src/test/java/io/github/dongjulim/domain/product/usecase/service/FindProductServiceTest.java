package io.github.dongjulim.domain.product.usecase.service;

import io.github.dongjulim.domain.product.dto.FindProductRequest;
import io.github.dongjulim.domain.product.dto.FindProductResponse;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.productCategory.entity.ProductCategory;
import io.github.dongjulim.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FindProductService findProductService;

    private Product product;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        User user = User.builder().id(1L).name("판매자").build();
        ProductCategory category = ProductCategory.builder().id(1L).name("식품").deleteCheck(false).build();
        product = Product.builder()
                .id(1L).userId(1L).name("사과").price(2000L).categoryId(1L).deleteCheck(false).build();
        ReflectionTestUtils.setField(product, "user", user);
        ReflectionTestUtils.setField(product, "category", category);
        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("findProduct - 키워드로 검색하면 해당 상품이 반환된다")
    void findProduct_shouldReturnProductsByKeyword() {
        FindProductRequest request = new FindProductRequest();
        request.setKeyword("사과");
        given(productRepository.findAllBySearchCondition(null, "사과", null, null, pageable))
                .willReturn(new PageImpl<>(List.of(product), pageable, 1));

        Page<FindProductResponse> result = findProductService.findProduct(request, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("사과");
    }

    @Test
    @DisplayName("findProduct - 카테고리로 필터링하면 해당 카테고리 상품이 반환된다")
    void findProduct_shouldReturnProductsByCategory() {
        FindProductRequest request = new FindProductRequest();
        request.setCategoryId(1L);
        given(productRepository.findAllBySearchCondition(1L, null, null, null, pageable))
                .willReturn(new PageImpl<>(List.of(product), pageable, 1));

        Page<FindProductResponse> result = findProductService.findProduct(request, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findProduct - 가격 범위로 필터링하면 해당 범위의 상품이 반환된다")
    void findProduct_shouldReturnProductsByPriceRange() {
        FindProductRequest request = new FindProductRequest();
        request.setMinPrice(1000L);
        request.setMaxPrice(5000L);
        given(productRepository.findAllBySearchCondition(null, null, 1000L, 5000L, pageable))
                .willReturn(new PageImpl<>(List.of(product), pageable, 1));

        Page<FindProductResponse> result = findProductService.findProduct(request, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getPrice()).isEqualTo(2000L);
    }

    @Test
    @DisplayName("findProduct - 검색 결과가 없으면 빈 페이지를 반환한다")
    void findProduct_shouldReturnEmptyPage_whenNoMatch() {
        FindProductRequest request = new FindProductRequest();
        request.setKeyword("없는상품");
        given(productRepository.findAllBySearchCondition(null, "없는상품", null, null, pageable))
                .willReturn(Page.empty(pageable));

        Page<FindProductResponse> result = findProductService.findProduct(request, pageable);

        assertThat(result.getContent()).isEmpty();
    }
}
