package io.github.dongjulim.api.scheduler;

import io.github.dongjulim.domain.coupon.usecase.NotifyCouponExpiryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponExpiryNotificationScheduler {

    private final NotifyCouponExpiryUseCase notifyCouponExpiryUseCase;

    @Scheduled(cron = "0 0 9 * * *")
    public void notifyCouponExpiry() {
        notifyCouponExpiryUseCase.notifyCouponExpiry();
    }
}
