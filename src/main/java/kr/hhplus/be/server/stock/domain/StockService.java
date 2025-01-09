package kr.hhplus.be.server.stock.domain;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void deductStock(DeductStockCommand command) {
        Stock stock = stockRepository.findByProductIdForUpdate(command.getProductId()).orElseThrow(() -> new EntityNotFoundException("제품이 없습니다"));
        stock.deduct(command.getAmount());
        stockRepository.save(stock);
    }
}
