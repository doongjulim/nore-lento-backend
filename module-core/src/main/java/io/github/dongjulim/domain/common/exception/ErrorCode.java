package io.github.dongjulim.domain.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND("user-not-found", "User Not Found"),
    PRODUCT_NOT_FOUND("product-not-found", "Product Not Found"),
    PRODUCT_CATEGORY_NOT_FOUND("product-category-not-found", "Product Category Not Found"),
    NOTICE_NOT_FOUND("notice-not-found", "Notice Not Found"),
    CART_NOT_FOUND("cart-not-found", "Cart Not Found"),
    CART_ITEM_NOT_FOUND("cart-item-not-found", "Cart Item Not Found"),
    ALREADY_LIKED("already-liked", "Already Liked"),
    NOTICE_LIKE_NOT_FOUND("notice-like-not-found", "Notice Like Not Found"),
    REVIEW_NOT_FOUND("review-not-found", "Review Not Found"),
    NOTIFICATION_NOT_FOUND("notification-not-found", "Notification Not Found"),
    ORDER_NOT_FOUND("order-not-found", "Order Not Found"),
    ORDER_NOT_CANCELLABLE("order-not-cancellable", "Order Not Cancellable"),
    CART_EMPTY("cart-empty", "Cart Is Empty"),
    PAYMENT_NOT_FOUND("payment-not-found", "Payment Not Found"),
    PAYMENT_NOT_REFUNDABLE("payment-not-refundable", "Payment Not Refundable"),
    ORDER_NOT_PAYABLE("order-not-payable", "Order Not Payable"),
    ORDER_NOT_COMPLETED("order-not-completed", "Order Not Completed"),
    ORDER_ALREADY_HAS_DELIVERY("order-already-has-delivery", "Order Already Has Delivery"),
    DELIVERY_NOT_FOUND("delivery-not-found", "Delivery Not Found"),
    DELIVERY_STATUS_NOT_ADVANCABLE("delivery-status-not-advancable", "Delivery Status Not Advancable"),
    DELIVERY_NOT_RETURNABLE("delivery-not-returnable", "Delivery Not Returnable"),
    OUT_OF_STOCK("out-of-stock", "Out Of Stock"),
    STOCK_NOT_FOUND("stock-not-found", "Stock Not Found"),
    SHIPPING_ADDRESS_NOT_FOUND("shipping-address-not-found", "Shipping Address Not Found");

    private final String code;
    private final String title;

    public String getTypeUri() {
        return "/api/errors/" + code;
    }
}
