package io.github.dongjulim.domain.common.exception;

public class ProductNotFoundException extends DomainException {

    public ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_FOUND, "존재하지 않는 상품입니다.");
    }
}
