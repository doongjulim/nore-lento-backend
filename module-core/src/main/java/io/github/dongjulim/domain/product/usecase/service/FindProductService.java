package io.github.dongjulim.domain.product.usecase.service;

import io.github.dongjulim.domain.product.dto.FindProductRequest;
import io.github.dongjulim.domain.product.dto.FindProductResponse;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.product.usecase.FindProductUseCase;
import io.github.dongjulim.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindProductService implements FindProductUseCase {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public Page<FindProductResponse> findProduct(FindProductRequest request, Pageable pageable) {
        return productRepository.findAllBySearchCondition(
                        request.getCategoryId(),
                        request.getKeyword(),
                        request.getMinPrice(),
                        request.getMaxPrice(),
                        pageable)
                .map(product -> {
                    double averageRating = Optional.ofNullable(
                            reviewRepository.findAverageRatingByProductId(product.getId())).orElse(0.0);
                    long reviewCount = reviewRepository.countByProductIdAndDeleteCheckFalse(product.getId());
                    return FindProductResponse.from(product, averageRating, reviewCount);
                });
    }
}
