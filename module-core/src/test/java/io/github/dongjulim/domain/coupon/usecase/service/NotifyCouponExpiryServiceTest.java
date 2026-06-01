package io.github.dongjulim.domain.coupon.usecase.service;

import io.github.dongjulim.domain.coupon.entity.Coupon;
import io.github.dongjulim.domain.coupon.entity.UserCoupon;
import io.github.dongjulim.domain.coupon.enums.DiscountType;
import io.github.dongjulim.domain.coupon.repository.CouponRepository;
import io.github.dongjulim.domain.coupon.repository.UserCouponRepository;
import io.github.dongjulim.domain.notification.entity.Notification;
import io.github.dongjulim.domain.notification.repository.NotificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class NotifyCouponExpiryServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private UserCouponRepository userCouponRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotifyCouponExpiryService notifyCouponExpiryService;

    @Test
    @DisplayName("notifyCouponExpiry - 만료 3일 이내 미사용 쿠폰이 있으면 해당 사용자에게 알림을 생성한다")
    void notifyCouponExpiry_shouldSendNotification_whenCouponExpiresSoon() {
        Coupon coupon = Coupon.builder().id(1L).name("10% 할인 쿠폰")
                .discountType(DiscountType.PERCENTAGE).discountValue(10L)
                .expiresAt(LocalDateTime.now().plusDays(2)).build();
        UserCoupon userCoupon = UserCoupon.builder().id(1L).userId(1L).couponId(1L).isUsed(false).build();

        given(couponRepository.findAllByExpiresAtBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .willReturn(List.of(coupon));
        given(userCouponRepository.findAllByCouponIdInAndIsUsedFalse(List.of(1L)))
                .willReturn(List.of(userCoupon));

        notifyCouponExpiryService.notifyCouponExpiry();

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        then(notificationRepository).should().save(captor.capture());
        assertThat(captor.getValue().getUserId()).isEqualTo(1L);
        assertThat(captor.getValue().getTitle()).isEqualTo("쿠폰 만료 임박 알림");
        assertThat(captor.getValue().getContent()).contains("10% 할인 쿠폰");
    }

    @Test
    @DisplayName("notifyCouponExpiry - 만료 임박 쿠폰이 없으면 알림이 생성되지 않는다")
    void notifyCouponExpiry_shouldNotSendNotification_whenNoCouponsExpiringSoon() {
        given(couponRepository.findAllByExpiresAtBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .willReturn(List.of());

        notifyCouponExpiryService.notifyCouponExpiry();

        then(notificationRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("notifyCouponExpiry - 만료 임박 쿠폰이 있어도 모두 사용된 쿠폰이면 알림이 생성되지 않는다")
    void notifyCouponExpiry_shouldNotSendNotification_whenAllCouponsAlreadyUsed() {
        Coupon coupon = Coupon.builder().id(1L).name("10% 할인 쿠폰")
                .discountType(DiscountType.PERCENTAGE).discountValue(10L)
                .expiresAt(LocalDateTime.now().plusDays(2)).build();

        given(couponRepository.findAllByExpiresAtBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .willReturn(List.of(coupon));
        given(userCouponRepository.findAllByCouponIdInAndIsUsedFalse(List.of(1L)))
                .willReturn(List.of());

        notifyCouponExpiryService.notifyCouponExpiry();

        then(notificationRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("notifyCouponExpiry - 쿠폰 1개에 여러 사용자가 있으면 각 사용자에게 알림이 생성된다")
    void notifyCouponExpiry_shouldSendNotificationToEachUser_whenMultipleUserHaveSameCoupon() {
        Coupon coupon = Coupon.builder().id(1L).name("1000원 할인 쿠폰")
                .discountType(DiscountType.FIXED).discountValue(1000L)
                .expiresAt(LocalDateTime.now().plusDays(1)).build();
        UserCoupon userCoupon1 = UserCoupon.builder().id(1L).userId(1L).couponId(1L).isUsed(false).build();
        UserCoupon userCoupon2 = UserCoupon.builder().id(2L).userId(2L).couponId(1L).isUsed(false).build();

        given(couponRepository.findAllByExpiresAtBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .willReturn(List.of(coupon));
        given(userCouponRepository.findAllByCouponIdInAndIsUsedFalse(List.of(1L)))
                .willReturn(List.of(userCoupon1, userCoupon2));

        notifyCouponExpiryService.notifyCouponExpiry();

        then(notificationRepository).should(org.mockito.Mockito.times(2)).save(any(Notification.class));
    }
}
