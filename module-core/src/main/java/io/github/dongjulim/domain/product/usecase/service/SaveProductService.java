package io.github.dongjulim.domain.product.usecase.service;

import io.github.dongjulim.domain.common.exception.UserNotFoundException;
import io.github.dongjulim.domain.product.dto.ProductRequest;
import io.github.dongjulim.domain.product.entity.Product;
import io.github.dongjulim.domain.product.repository.ProductRepository;
import io.github.dongjulim.domain.product.usecase.SaveProductUseCase;
import io.github.dongjulim.domain.user.entity.User;
import io.github.dongjulim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SaveProductService implements SaveProductUseCase {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public void saveProduct(ProductRequest request, String username) {
        User user = userRepository.findByUsernameAndDeleteCheck(username, false)
                .orElseThrow(UserNotFoundException::new);
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
