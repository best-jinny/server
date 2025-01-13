package kr.hhplus.be.server.point.facade;

import kr.hhplus.be.server.point.domain.PointResult;
import kr.hhplus.be.server.point.domain.PointService;
import kr.hhplus.be.server.user.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointFacade {
    private final PointService pointService;
    private final UserService userService;

    public PointResult charge(Long userId, Long amount) {
        userService.verify(userId);
        return pointService.charge(userId, amount);
    }

    public PointResult getBalance(Long userId) {
        userService.verify(userId);
        return pointService.getBalance(userId);
    }

}
