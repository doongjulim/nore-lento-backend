package io.github.dongjulim.domain.common.exception;

public class CartItemNotFoundException extends DomainException {

    public CartItemNotFoundException() {
        super(ErrorCode.CART_ITEM_NOT_FOUND, "존재하지 않는 장바구니 아이템입니다.");
    }
}
