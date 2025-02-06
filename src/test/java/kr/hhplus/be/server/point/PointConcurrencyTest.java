package kr.hhplus.be.server.point;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.hhplus.be.server.point.domain.Point;
import kr.hhplus.be.server.point.domain.PointRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PointConcurrencyTest {

    @Autowired
    private PointRepository pointRepository;

    @AfterEach
    void tearDown() {
        pointRepository.deleteAll();
    }
    
    @Test
    @DisplayName("동시에 포인트 충전과 차감이 이뤄지면 하나는 실패하고 하나는 성공한다")
    void testPointConcurrency() throws InterruptedException {
        Long userId = 10L;
        Long amount = 10000L;

        Point point = Point.builder()
                .userId(userId)
                .point(amount)
                .build();
        pointRepository.save(point);

        // 같은 엔티티의 두 인스턴스 생성
        Point point1 = pointRepository.findByUserId(userId).orElseThrow();
        Point point2 = pointRepository.findByUserId(userId).orElseThrow();

        // 예외 캡처를 위한 AtomicReference 준비
        AtomicReference<Throwable> thread1Exception = new AtomicReference<>();
        AtomicReference<Throwable> thread2Exception = new AtomicReference<>();

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> {
            try {
                point1.deduct(5000L); // 5000 포인트 차감
                pointRepository.save(point1);
            } catch (Throwable e) {
                thread1Exception.set(e);
            }
        });

        executorService.submit(() -> {
            try {
                point2.charge(3000L); // 3000 포인트 충전
                pointRepository.save(point2);
            } catch (Throwable e) {
                thread2Exception.set(e);
            }
        });

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        // 결과 및 예외 확인
        Point finalPoint = pointRepository.findByUserId(userId).orElseThrow();

        // 예외 리스트 수집
        List<Throwable> exceptions = Stream.of(thread1Exception.get(), thread2Exception.get())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 하나의 스레드에서만 예외 발생 확인
        assertThat(exceptions).hasSize(1);
        assertThat(exceptions.get(0)).isInstanceOf(ObjectOptimisticLockingFailureException.class);

        // 최종 포인트 값 검증 (차감이 성공이면 5000 포인트, 충전이 성공이면 13000 포인트)
        assertThat(finalPoint.getPoint()).isIn(5000L, 13000L);
    }

}
