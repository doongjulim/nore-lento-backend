package io.github.dongjulim.domain.productCategory.usecase.service;

import io.github.dongjulim.domain.productCategory.dto.FindProductCategoryResponse;
import io.github.dongjulim.domain.productCategory.repository.ProductCategoryRepository;
import io.github.dongjulim.domain.productCategory.usecase.FindProductCategoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindProductCategoryService implements FindProductCategoryUseCase {

    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public List<FindProductCategoryResponse> findProductCategories() {
        return productCategoryRepository.findAllByDeleteCheckFalse().stream()
                .map(FindProductCategoryResponse::from)
                .collect(Collectors.toList());
    }
}
