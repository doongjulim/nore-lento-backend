package io.github.dongjulim.domain.common.exception;

public class CartEmptyException extends DomainException {

    public CartEmptyException() {
        super(ErrorCode.CART_EMPTY, "장바구니가 비어있습니다.");
    }
}
