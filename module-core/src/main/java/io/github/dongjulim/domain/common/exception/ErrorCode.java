package io.github.dongjulim.domain.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND("user-not-found", "User Not Found"),
    PRODUCT_NOT_FOUND("product-not-found", "Product Not Found"),
    NOTICE_NOT_FOUND("notice-not-found", "Notice Not Found"),
    CART_NOT_FOUND("cart-not-found", "Cart Not Found"),
    CART_ITEM_NOT_FOUND("cart-item-not-found", "Cart Item Not Found"),
    ALREADY_LIKED("already-liked", "Already Liked"),
    NOTICE_LIKE_NOT_FOUND("notice-like-not-found", "Notice Like Not Found");

    private final String code;
    private final String title;

    public String getTypeUri() {
        return "/api/errors/" + code;
    }
}
