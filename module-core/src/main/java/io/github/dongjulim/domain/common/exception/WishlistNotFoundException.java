package io.github.dongjulim.domain.common.exception;

public class WishlistNotFoundException extends DomainException {

    public WishlistNotFoundException() {
        super(ErrorCode.WISHLIST_NOT_FOUND, "찜 항목을 찾을 수 없습니다.");
    }
}
