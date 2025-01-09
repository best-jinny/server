package kr.hhplus.be.server.stock.infra;

import kr.hhplus.be.server.stock.domain.Stock;
import kr.hhplus.be.server.stock.domain.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StockRepositoryImpl implements StockRepository {

    private final StockJpaRepository stockJpaRepository;

    @Override
    public Optional<Stock> findByProductIdForUpdate(Long productId) {
        return stockJpaRepository.findByProductIdForUpdate(productId);
    }

    @Override
    public void save(Stock stock) {
        stockJpaRepository.save(stock);
    }
}
