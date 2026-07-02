package io.github.dongjulim.domain.product.usecase.service;

import io.github.dongjulim.domain.common.exception.ProductCategoryNotFoundException;
import io.github.dongjulim.domain.product.dto.SaveProductRequest;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.productCategory.entity.ProductCategory;
import io.github.dongjulim.domain.productCategory.repository.ProductCategoryRepository;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.product.usecase.SaveProductUseCase;
import io.github.dongjulim.domain.stock.entity.Stock;
import io.github.dongjulim.domain.stock.repository.StockRepository;
import io.github.dongjulim.domain.user.component.UserLoader;
import io.github.dongjulim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SaveProductService implements SaveProductUseCase {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final StockRepository stockRepository;
    private final UserLoader userLoader;

    @Override
    public void saveProduct(SaveProductRequest request, String imageUrl, String username) {
        User user = userLoader.load(username);
        ProductCategory category = productCategoryRepository.findByIdAndDeleteCheckFalse(request.getCategoryId())
                .orElseThrow(ProductCategoryNotFoundException::new);
        Product product = productRepository.save(Product.builder()
                .userId(user.getId())
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .categoryId(category.getId())
                .imageUrl(imageUrl)
                .build());
        stockRepository.save(Stock.builder()
                .productId(product.getId())
                .quantity(request.getStock())
                .build());
    }
}
