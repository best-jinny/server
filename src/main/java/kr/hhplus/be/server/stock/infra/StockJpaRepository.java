package kr.hhplus.be.server.stock.infra;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StockJpaRepository extends JpaRepository<Stock, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Stock s WHERE s.id = :productId")
    Optional<Stock> findByProductIdForUpdate(@Param("productId") Long productId);

}
