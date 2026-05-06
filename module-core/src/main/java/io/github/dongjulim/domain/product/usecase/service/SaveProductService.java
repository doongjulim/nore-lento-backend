package io.github.dongjulim.domain.product.usecase.service;

import io.github.dongjulim.domain.product.dto.SaveProductRequest;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.product.usecase.SaveProductUseCase;
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
    private final UserLoader userLoader;

    @Override
    public void saveProduct(SaveProductRequest request, String username) {
        User user = userLoader.load(username);
        Product product = Product.builder()
                .userId(user.getId())
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .category(request.getCategory())
                .build();
        productRepository.save(product);
    }
}
