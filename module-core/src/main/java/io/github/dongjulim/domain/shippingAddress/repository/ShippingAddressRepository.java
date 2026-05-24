package io.github.dongjulim.domain.shippingAddress.repository;

import io.github.dongjulim.domain.shippingAddress.entity.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {

    List<ShippingAddress> findAllByUserId(Long userId);

    Optional<ShippingAddress> findByIdAndUserId(Long id, Long userId);

    Optional<ShippingAddress> findByUserIdAndIsDefaultTrue(Long userId);
}
