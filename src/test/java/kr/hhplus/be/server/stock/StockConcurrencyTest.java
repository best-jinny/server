package kr.hhplus.be.server.stock;

import jakarta.persistence.EntityNotFoundException;
import kr.hhplus.be.server.common.exceptions.NotFoundException;
import kr.hhplus.be.server.stock.domain.DeductStockCommand;
import kr.hhplus.be.server.stock.domain.Stock;
import kr.hhplus.be.server.stock.domain.StockRepository;
import kr.hhplus.be.server.stock.domain.StockService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class StockConcurrencyTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    @Test
    @DisplayName("동시에 10개의 스레드에서 10개의 재고를 차감하면 0개가 남아야한다")
    void testStockConcurrency() throws InterruptedException {

        Long productId = 1L;
        int quantity = 100;

        Stock stock = Stock.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
        stockRepository.save(stock);

        int threadCount = 10;
        int deductionAmount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    DeductStockCommand command = new DeductStockCommand(productId, deductionAmount);
                    stockService.deductStock(command);
                } catch (Exception e) {
                    System.err.println("예외발생!!" + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        Stock afterStock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품이 없습니다"));
        assertThat(afterStock.getQuantity()).isEqualTo(0);

    }

    @Test
    @DisplayName("동시에 10개의 스레드에서 15개의 재고를 차감하면 최종 재고는 10개가 남고 성공 횟수 6, 실패 4 가 기록되어야한다")
    void testOverDeduction() throws InterruptedException {

        Long productId = 2L;
        int quantity = 100;

        Stock stock = Stock.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
        stockRepository.save(stock);

        int threadCount = 10;
        int deductionAmount = 15;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0); // 성공 카운터
        AtomicInteger failureCount = new AtomicInteger(0); // 실패 카운터

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    DeductStockCommand command = new DeductStockCommand(productId, deductionAmount);
                    stockService.deductStock(command);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                    System.err.println("예외 발생: " + e.getMessage());
                } finally {
                    latch.countDown(); // 작업 완료
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // 결과
        Stock afterStock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException("상품이 없습니다"));

        // 검증: 최종 재고는 10 이어야 함
        assertThat(afterStock.getQuantity()).isEqualTo(10);

        // 검증: 성공 요청은 6번, 실패 요청은 4번이어야 함
        assertThat(successCount.get()).isEqualTo(6);
        assertThat(failureCount.get()).isEqualTo(4);
    }
}
