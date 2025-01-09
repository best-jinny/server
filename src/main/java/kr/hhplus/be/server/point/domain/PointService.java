package kr.hhplus.be.server.point.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    /* 잔액 차감 */
    public void deduct (Long userId, Long amount) {
        Point point = pointRepository.findByUserId(userId).orElseThrow(IllegalArgumentException::new);
        point.deduct(amount);
        pointRepository.save(point);
    }

}
