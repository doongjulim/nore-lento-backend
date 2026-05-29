package io.github.dongjulim.domain.order.repository;

import io.github.dongjulim.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findAllByOrderId(Long orderId);

    @Query("SELECT CASE WHEN COUNT(oi) > 0 THEN true ELSE false END FROM order_item oi " +
           "WHERE oi.order.userId = :userId AND oi.productId = :productId AND oi.order.status = 'COMPLETED'")
    boolean existsCompletedOrderByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}
