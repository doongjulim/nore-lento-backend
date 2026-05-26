package io.github.dongjulim.domain.common.exception;

public class AlreadyWishlistedException extends DomainException {

    public AlreadyWishlistedException() {
        super(ErrorCode.ALREADY_WISHLISTED, "이미 찜한 상품입니다.");
    }
}
