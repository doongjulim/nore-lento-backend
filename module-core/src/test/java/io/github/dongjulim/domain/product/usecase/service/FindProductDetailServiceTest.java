package io.github.dongjulim.domain.product.usecase.service;

import io.github.dongjulim.domain.common.exception.ProductNotFoundException;
import io.github.dongjulim.domain.product.dto.FindProductDetailResponse;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.productCategory.entity.ProductCategory;
import io.github.dongjulim.domain.review.repository.ReviewRepository;
import io.github.dongjulim.domain.stock.entity.Stock;
import io.github.dongjulim.domain.stock.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
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
class FindProductDetailServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private FindProductDetailService findProductDetailService;

    private Product product;

    @BeforeEach
    void setUp() {
        ProductCategory category = ProductCategory.builder().id(5L).name("식품").deleteCheck(false).build();
        product = Product.builder()
                .id(1L).userId(1L).name("사과").price(2000L).description("맛있는 사과")
                .categoryId(5L).deleteCheck(false).build();
        ReflectionTestUtils.setField(product, "category", category);
    }

    @Test
    @DisplayName("findProductDetail - 상품 상세 정보와 재고 수량을 함께 반환한다")
    void findProductDetail_shouldReturnProductDetailWithStock() {
        Stock stock = Stock.builder().id(1L).productId(1L).quantity(42).build();

        given(productRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(1L)).willReturn(Optional.of(stock));

        FindProductDetailResponse result = findProductDetailService.findProductDetail(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("사과");
        assertThat(result.getPrice()).isEqualTo(2000L);
        assertThat(result.getStock()).isEqualTo(42);
    }

    @Test
    @DisplayName("findProductDetail - 리뷰가 있으면 평균 평점과 리뷰 수를 반환한다")
    void findProductDetail_shouldReturnAverageRatingAndReviewCount() {
        given(productRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(1L)).willReturn(Optional.empty());
        given(reviewRepository.findAverageRatingByProductId(1L)).willReturn(4.5);
        given(reviewRepository.countByProductIdAndDeleteCheckFalse(1L)).willReturn(10L);

        FindProductDetailResponse result = findProductDetailService.findProductDetail(1L);

        assertThat(result.getAverageRating()).isEqualTo(4.5);
        assertThat(result.getReviewCount()).isEqualTo(10L);
    }

    @Test
    @DisplayName("findProductDetail - 리뷰가 없으면 평균 평점 0.0, 리뷰 수 0을 반환한다")
    void findProductDetail_shouldReturnZero_whenNoReviews() {
        given(productRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(1L)).willReturn(Optional.empty());
        given(reviewRepository.findAverageRatingByProductId(1L)).willReturn(null);
        given(reviewRepository.countByProductIdAndDeleteCheckFalse(1L)).willReturn(0L);

        FindProductDetailResponse result = findProductDetailService.findProductDetail(1L);

        assertThat(result.getAverageRating()).isEqualTo(0.0);
        assertThat(result.getReviewCount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("findProductDetail - 재고 정보가 없으면 재고를 0으로 반환한다")
    void findProductDetail_shouldReturn0_whenStockNotFound() {
        given(productRepository.findByIdAndDeleteCheckFalse(1L)).willReturn(Optional.of(product));
        given(stockRepository.findByProductId(1L)).willReturn(Optional.empty());

        FindProductDetailResponse result = findProductDetailService.findProductDetail(1L);

        assertThat(result.getStock()).isEqualTo(0);
    }

    @Test
    @DisplayName("findProductDetail - 존재하지 않는 상품이면 ProductNotFoundException을 던진다")
    void findProductDetail_throwsProductNotFoundException_whenProductNotFound() {
        given(productRepository.findByIdAndDeleteCheckFalse(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> findProductDetailService.findProductDetail(99L))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
