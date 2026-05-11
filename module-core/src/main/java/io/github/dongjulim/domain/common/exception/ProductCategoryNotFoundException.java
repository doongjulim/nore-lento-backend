package io.github.dongjulim.domain.common.exception;

public class ProductCategoryNotFoundException extends DomainException {

    public ProductCategoryNotFoundException() {
        super(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND, "존재하지 않는 카테고리입니다.");
    }
}
