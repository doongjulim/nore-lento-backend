package io.github.dongjulim.domain.productCategory.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductCategoryTest {

    private ProductCategory createCategory() {
        return ProductCategory.builder()
                .name("식품")
                .deleteCheck(false)
                .build();
    }

    @Test
    @DisplayName("updateName - 카테고리 이름이 정상적으로 변경된다")
    void updateName_shouldUpdateName() {
        ProductCategory category = createCategory();

        category.updateName("전자기기");

        assertThat(category.getName()).isEqualTo("전자기기");
    }

    @Test
    @DisplayName("updateName - 이름 변경 시 deleteCheck는 유지된다")
    void updateName_shouldNotChangeDeleteCheck() {
        ProductCategory category = createCategory();

        category.updateName("의류");

        assertThat(category.getDeleteCheck()).isFalse();
    }

    @Test
    @DisplayName("delete - deleteCheck가 true로 변경된다")
    void delete_shouldSetDeleteCheckTrue() {
        ProductCategory category = createCategory();

        category.delete();

        assertThat(category.getDeleteCheck()).isTrue();
    }
}
