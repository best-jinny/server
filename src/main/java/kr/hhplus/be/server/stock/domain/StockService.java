package kr.hhplus.be.server.stock.domain;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;

    public void deductStocks(List<DeductStockCommand> commands) {
        commands.forEach(this::deductStock);
    }


    @Transactional
    public void deductStock(DeductStockCommand command) {
        Stock stock = stockRepository.findByProductIdForUpdate(command.getProductId()).orElseThrow(() -> new EntityNotFoundException("제품이 없습니다"));
        stock.deduct(command.getAmount());
        stockRepository.save(stock);
    }
}
