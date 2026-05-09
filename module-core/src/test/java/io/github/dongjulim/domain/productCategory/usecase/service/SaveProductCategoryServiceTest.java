package io.github.dongjulim.domain.productCategory.usecase.service;

import io.github.dongjulim.domain.productCategory.dto.SaveProductCategoryRequest;
import io.github.dongjulim.domain.productCategory.entity.ProductCategory;
import io.github.dongjulim.domain.productCategory.repository.ProductCategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SaveProductCategoryServiceTest {

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @InjectMocks
    private SaveProductCategoryService saveProductCategoryService;

    @Test
    @DisplayName("saveProductCategory - 카테고리를 저장한다")
    void saveProductCategory_shouldSaveCategory() {
        SaveProductCategoryRequest request = new SaveProductCategoryRequest();
        ReflectionTestUtils.setField(request, "name", "식품");

        saveProductCategoryService.saveProductCategory(request);

        ArgumentCaptor<ProductCategory> captor = ArgumentCaptor.forClass(ProductCategory.class);
        then(productCategoryRepository).should().save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("식품");
        assertThat(captor.getValue().getDeleteCheck()).isFalse();
    }
}
