package io.github.dongjulim.domain.common.exception;

public class CartNotFoundException extends DomainException {

    public CartNotFoundException() {
        super(ErrorCode.CART_NOT_FOUND, "존재하지 않는 장바구니입니다.");
    }
}
