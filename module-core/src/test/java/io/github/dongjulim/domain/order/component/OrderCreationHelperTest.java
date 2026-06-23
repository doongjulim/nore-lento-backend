package io.github.dongjulim.domain.order.component;

import io.github.dongjulim.domain.common.exception.CouponAlreadyUsedException;
import io.github.dongjulim.domain.common.exception.CouponExpiredException;
import io.github.dongjulim.domain.common.exception.CouponNotFoundException;
import io.github.dongjulim.domain.common.exception.InsufficientPointException;
import io.github.dongjulim.domain.common.exception.OrderAmountNotEnoughException;
import io.github.dongjulim.domain.common.exception.ShippingAddressNotFoundException;
import io.github.dongjulim.domain.coupon.entity.Coupon;
import io.github.dongjulim.domain.coupon.entity.UserCoupon;
import io.github.dongjulim.domain.coupon.enums.DiscountType;
import io.github.dongjulim.domain.coupon.repository.CouponRepository;
import io.github.dongjulim.domain.coupon.repository.UserCouponRepository;
import io.github.dongjulim.domain.point.entity.UserPoint;
import io.github.dongjulim.domain.point.repository.PointHistoryRepository;
import io.github.dongjulim.domain.point.repository.UserPointRepository;
import io.github.dongjulim.domain.shippingAddress.entity.ShippingAddress;
import io.github.dongjulim.domain.shippingAddress.repository.ShippingAddressRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class OrderCreationHelperTest {

    @Mock
    private ShippingAddressRepository shippingAddressRepository;

    @Mock
    private UserCouponRepository userCouponRepository;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private UserPointRepository userPointRepository;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @InjectMocks
    private OrderCreationHelper orderCreationHelper;

    // ── resolveShippingAddressId ──────────────────────────────────────────────

    @Test
    @DisplayName("resolveShippingAddressId - 요청 배송지 ID가 있으면 해당 ID를 반환한다")
    void resolveShippingAddressId_shouldReturnRequestedId_whenProvided() {
        ShippingAddress address = ShippingAddress.builder().id(1L).userId(1L)
                .recipientName("홍길동").phone("010-1234-5678").address("서울시 강남구").zipCode("12345").build();

        given(shippingAddressRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(address));

        Long result = orderCreationHelper.resolveShippingAddressId(1L, 1L);

        assertThat(result).isEqualTo(1L);
    }

    @Test
    @DisplayName("resolveShippingAddressId - 요청 배송지 ID가 null이면 기본 배송지를 반환한다")
    void resolveShippingAddressId_shouldReturnDefaultAddressId_whenRequestedIdIsNull() {
        ShippingAddress defaultAddress = ShippingAddress.builder().id(5L).userId(1L)
                .recipientName("홍길동").phone("010-1234-5678").address("부산시 해운대구").zipCode("54321").build();

        given(shippingAddressRepository.findByUserIdAndIsDefaultTrue(1L)).willReturn(Optional.of(defaultAddress));

        Long result = orderCreationHelper.resolveShippingAddressId(null, 1L);

        assertThat(result).isEqualTo(5L);
    }

    @Test
    @DisplayName("resolveShippingAddressId - 존재하지 않는 배송지면 ShippingAddressNotFoundException을 던진다")
    void resolveShippingAddressId_throwsShippingAddressNotFoundException_whenNotFound() {
        given(shippingAddressRepository.findByIdAndUserId(99L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderCreationHelper.resolveShippingAddressId(99L, 1L))
                .isInstanceOf(ShippingAddressNotFoundException.class);
    }

    @Test
    @DisplayName("resolveShippingAddressId - null ID인데 기본 배송지도 없으면 ShippingAddressNotFoundException을 던진다")
    void resolveShippingAddressId_throwsShippingAddressNotFoundException_whenNoDefaultAddress() {
        given(shippingAddressRepository.findByUserIdAndIsDefaultTrue(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderCreationHelper.resolveShippingAddressId(null, 1L))
                .isInstanceOf(ShippingAddressNotFoundException.class);
    }

    // ── applyCoupon ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("applyCoupon - 정액 쿠폰 적용 시 할인된 금액을 반환하고 쿠폰이 사용 처리된다")
    void applyCoupon_shouldApplyFixedDiscount_andMarkCouponUsed() {
        UserCoupon userCoupon = UserCoupon.builder().id(1L).userId(1L).couponId(10L).isUsed(false).build();
        Coupon coupon = Coupon.builder().id(10L).name("1000원 할인").discountType(DiscountType.FIXED).discountValue(1000L).build();

        given(userCouponRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(userCoupon));
        given(couponRepository.findById(10L)).willReturn(Optional.of(coupon));

        long result = orderCreationHelper.applyCoupon(1L, 1L, 6000L);

        assertThat(result).isEqualTo(5000L);
        assertThat(userCoupon.getIsUsed()).isTrue();
    }

    @Test
    @DisplayName("applyCoupon - 퍼센트 쿠폰 적용 시 비율만큼 할인된 금액을 반환한다")
    void applyCoupon_shouldApplyPercentageDiscount() {
        UserCoupon userCoupon = UserCoupon.builder().id(1L).userId(1L).couponId(10L).isUsed(false).build();
        Coupon coupon = Coupon.builder().id(10L).name("10% 할인").discountType(DiscountType.PERCENTAGE).discountValue(10L).build();

        given(userCouponRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(userCoupon));
        given(couponRepository.findById(10L)).willReturn(Optional.of(coupon));

        long result = orderCreationHelper.applyCoupon(1L, 1L, 6000L);

        assertThat(result).isEqualTo(5400L); // 6000 * 90 / 100
    }

    @Test
    @DisplayName("applyCoupon - 쿠폰이 존재하지 않으면 CouponNotFoundException을 던진다")
    void applyCoupon_throwsCouponNotFoundException_whenCouponNotFound() {
        given(userCouponRepository.findByIdAndUserId(99L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderCreationHelper.applyCoupon(99L, 1L, 6000L))
                .isInstanceOf(CouponNotFoundException.class);
    }

    @Test
    @DisplayName("applyCoupon - 이미 사용된 쿠폰이면 CouponAlreadyUsedException을 던진다")
    void applyCoupon_throwsCouponAlreadyUsedException_whenAlreadyUsed() {
        UserCoupon usedCoupon = UserCoupon.builder().id(1L).userId(1L).couponId(10L).isUsed(true).build();

        given(userCouponRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(usedCoupon));

        assertThatThrownBy(() -> orderCreationHelper.applyCoupon(1L, 1L, 6000L))
                .isInstanceOf(CouponAlreadyUsedException.class);
    }

    @Test
    @DisplayName("applyCoupon - 만료된 쿠폰이면 CouponExpiredException을 던진다")
    void applyCoupon_throwsCouponExpiredException_whenExpired() {
        UserCoupon userCoupon = UserCoupon.builder().id(1L).userId(1L).couponId(10L).isUsed(false).build();
        Coupon expiredCoupon = Coupon.builder().id(10L).name("만료쿠폰").discountType(DiscountType.FIXED)
                .discountValue(1000L).expiresAt(LocalDateTime.now().minusDays(1)).build();

        given(userCouponRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(userCoupon));
        given(couponRepository.findById(10L)).willReturn(Optional.of(expiredCoupon));

        assertThatThrownBy(() -> orderCreationHelper.applyCoupon(1L, 1L, 6000L))
                .isInstanceOf(CouponExpiredException.class);
    }

    @Test
    @DisplayName("applyCoupon - 최소 주문 금액 미달이면 OrderAmountNotEnoughException을 던진다")
    void applyCoupon_throwsOrderAmountNotEnoughException_whenAmountBelowMinimum() {
        UserCoupon userCoupon = UserCoupon.builder().id(1L).userId(1L).couponId(10L).isUsed(false).build();
        Coupon coupon = Coupon.builder().id(10L).name("할인쿠폰").discountType(DiscountType.FIXED)
                .discountValue(1000L).minOrderAmount(5000L).build();

        given(userCouponRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(userCoupon));
        given(couponRepository.findById(10L)).willReturn(Optional.of(coupon));

        assertThatThrownBy(() -> orderCreationHelper.applyCoupon(1L, 1L, 2000L))
                .isInstanceOf(OrderAmountNotEnoughException.class);
    }

    // ── applyPoints ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("applyPoints - 포인트 사용 시 차감된 총액을 반환하고 이력이 저장된다")
    void applyPoints_shouldDeductPointsAndSaveHistory() {
        UserPoint userPoint = UserPoint.builder().userId(1L).balance(2000L).build();

        given(userPointRepository.findByUserId(1L)).willReturn(Optional.of(userPoint));

        long result = orderCreationHelper.applyPoints(1000L, 1L, 6000L);

        assertThat(result).isEqualTo(5000L);
        assertThat(userPoint.getBalance()).isEqualTo(1000L);
        then(pointHistoryRepository).should().save(any());
    }

    @Test
    @DisplayName("applyPoints - 포인트 잔액이 부족하면 InsufficientPointException을 던진다")
    void applyPoints_throwsInsufficientPointException_whenBalanceNotEnough() {
        UserPoint userPoint = UserPoint.builder().userId(1L).balance(500L).build();

        given(userPointRepository.findByUserId(1L)).willReturn(Optional.of(userPoint));

        assertThatThrownBy(() -> orderCreationHelper.applyPoints(3000L, 1L, 6000L))
                .isInstanceOf(InsufficientPointException.class);
    }
}
