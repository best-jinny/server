package kr.hhplus.be.server.coupon.domain;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.common.exceptions.InvalidException;
import kr.hhplus.be.server.common.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;

    public Long calculateDiscountAmount(Long issuedCouponId, Long orderAmount) {
        IssuedCoupon issuedCoupon = issuedCouponRepository.findByIdForUpdate(issuedCouponId)
                .orElseThrow(() -> new InvalidException("해당 쿠폰을 찾을 수 없습니다."));

        issuedCoupon.validate();
        Coupon coupon = issuedCoupon.getCoupon();
        return coupon.calculateDiscountAmount(orderAmount);
    }

    @Transactional
    public void redeemCoupon(Long issuedCouponId) {
        IssuedCoupon issuedCoupon = issuedCouponRepository.findById(issuedCouponId)
                .orElseThrow(() -> new InvalidException("해당 쿠폰을 찾을 수 없습니다."));
        issuedCoupon.markAsUsed();
        issuedCouponRepository.save(issuedCoupon);
    }

    @Transactional
    public IssuedCouponResult issueCoupon(IssueCouponCommand issueCouponCommand) {
        Coupon coupon = getCouponWithLock(issueCouponCommand.getCouponId());
        boolean alreadyIssued = issuedCouponRepository.existsByCouponIdAndUserId(coupon.getId(), issueCouponCommand.getUserId());
        if (alreadyIssued) {
            throw new InvalidException("이미 발급된 쿠폰입니다.");
        }
        try {
            IssuedCoupon issuedCoupon = IssuedCoupon.issue(coupon, issueCouponCommand.getUserId(), LocalDateTime.now().plusDays(30));
            issuedCouponRepository.save(issuedCoupon);
            return IssuedCouponResult.of(issuedCoupon);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidException("이미 발급된 쿠폰입니다.");
        }
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Coupon getCouponWithLock(Long couponId) {
        return couponRepository.findByIdForUpdate(couponId)
                .orElseThrow(() -> new NotFoundException("쿠폰을 찾을 수 없습니다."));
    }

    public Page<IssuedCouponResult> getIssuedCouponsByUserId(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "issuedAt")); // 최신 발급순 정렬

        Page<IssuedCoupon> issuedCouponPage = issuedCouponRepository.findAllByUserId(userId, pageable);
        return issuedCouponPage.map(IssuedCouponResult::of);
    }
}
