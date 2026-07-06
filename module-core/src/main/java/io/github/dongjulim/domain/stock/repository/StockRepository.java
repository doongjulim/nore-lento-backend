package io.github.dongjulim.domain.stock.repository;

import io.github.dongjulim.domain.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByProductId(Long productId);

    List<Stock> findAllByProductIdIn(List<Long> productIds);
}
