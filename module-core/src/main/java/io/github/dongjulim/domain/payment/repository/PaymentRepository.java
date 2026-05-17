package io.github.dongjulim.domain.payment.repository;

import io.github.dongjulim.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByIdAndUserId(Long id, Long userId);

    List<Payment> findAllByUserId(Long userId);

    boolean existsByOrderId(Long orderId);
}
