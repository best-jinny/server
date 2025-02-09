package kr.hhplus.be.server.coupon.domain;

import kr.hhplus.be.server.common.exceptions.InvalidException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CouponIssuanceService {

    private final Map<String, CouponIssuanceStrategy> strategyMap;

    public CouponIssuanceService(List<CouponIssuanceStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(CouponIssuanceStrategy::getType, Function.identity()));
    }

    public CouponIssuanceResponse issueCoupon(String strategyType, IssueCouponCommand command) {
        CouponIssuanceStrategy strategy = strategyMap.get(strategyType);
        if (strategy == null) {
            throw new InvalidException("존재하지 않는 전략 : " + strategyType);
        }
        return strategy.issueCoupon(command);
    }


}
