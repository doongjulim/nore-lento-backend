package io.github.dongjulim.domain.product.usecase.service;

import io.github.dongjulim.domain.common.exception.ProductNotFoundException;
import io.github.dongjulim.domain.product.dto.FindProductDetailResponse;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.product.usecase.FindProductDetailUseCase;
import io.github.dongjulim.domain.review.repository.ReviewRepository;
import io.github.dongjulim.domain.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindProductDetailService implements FindProductDetailUseCase {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public FindProductDetailResponse findProductDetail(Long id) {
        Product product = productRepository.findByIdAndDeleteCheckFalse(id)
                .orElseThrow(ProductNotFoundException::new);
        int stock = stockRepository.findByProductId(id)
                .map(s -> s.getQuantity())
                .orElse(0);
        double averageRating = Optional.ofNullable(reviewRepository.findAverageRatingByProductId(id))
                .orElse(0.0);
        long reviewCount = reviewRepository.countByProductIdAndDeleteCheckFalse(id);
        return FindProductDetailResponse.from(product, stock, averageRating, reviewCount);
    }
}
