package kr.hhplus.be.server.coupon.domain;

import kr.hhplus.be.server.common.exceptions.InvalidException;
import kr.hhplus.be.server.common.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DatabaseCouponIssuanceStrategy implements CouponIssuanceStrategy{

    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;

    @Override
    @Transactional
    public CouponIssuanceResponse issueCoupon(IssueCouponCommand command) {
        Coupon coupon = couponRepository.findByIdForUpdate(command.getCouponId())
                .orElseThrow(() -> new NotFoundException("쿠폰을 찾을 수 없습니다."));
        boolean alreadyIssued = issuedCouponRepository.existsByCouponIdAndUserId(coupon.getId(), command.getUserId());
        if (alreadyIssued) {
            throw new InvalidException("이미 발급된 쿠폰입니다.");
        }
        try {
            IssuedCoupon issuedCoupon = IssuedCoupon.issue(coupon, command.getUserId(), LocalDateTime.now().plusDays(30));
            issuedCouponRepository.save(issuedCoupon);
            couponRepository.save(coupon);
            IssuedCouponResult result = IssuedCouponResult.of(issuedCoupon);
            return new CouponIssuanceResponse(CouponIssuanceStatus.COMPLETED, "쿠폰 발급이 완료되었습니다", result);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidException("이미 발급된 쿠폰입니다.");
        }
    }

    @Override
    public String getType() {
        return CouponStrategy.DB.name();
    }
}
