package kr.hhplus.be.server.stock.domain;

import java.util.Optional;

public interface StockRepository {
    Optional<Stock> findByProductId(Long productId);
    Optional<Stock> findByProductIdForUpdate(Long productId);
    void save(Stock stock);
    void deleteAll();

}
