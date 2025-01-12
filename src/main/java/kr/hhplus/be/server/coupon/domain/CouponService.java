package kr.hhplus.be.server.coupon.domain;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.common.exceptions.InvalidCouponException;
import kr.hhplus.be.server.user.domain.User;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;

    public Long calculateDiscountAmount(Long issuedCouponId, Long orderAmount) {
        IssuedCoupon issuedCoupon = issuedCouponRepository.findByIdForUpdate(issuedCouponId)
                .orElseThrow(() -> new InvalidCouponException("해당 쿠폰을 찾을 수 없습니다."));

        issuedCoupon.validate();
        Coupon coupon = issuedCoupon.getCoupon();
        return coupon.calculateDiscountAmount(orderAmount);
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void redeemCoupon(Long issuedCouponId) {
        IssuedCoupon issuedCoupon = issuedCouponRepository.findByIdForUpdate(issuedCouponId)
                .orElseThrow(() -> new InvalidCouponException("해당 쿠폰을 찾을 수 없습니다."));
        issuedCoupon.markAsUsed();
        issuedCouponRepository.save(issuedCoupon);
    }

    @Transactional
    public IssuedCouponResult issueCoupon(Long couponId, Long userId) {
        Coupon coupon = getCouponWithLock(couponId);
        boolean alreadyIssued = issuedCouponRepository.existsByCouponIdAndUserId(coupon.getId(), userId);
        if (alreadyIssued) {
            throw new IllegalStateException("이미 발급된 쿠폰입니다.");
        }
        try {
            IssuedCoupon issuedCoupon = IssuedCoupon.issue(coupon, userId, LocalDateTime.now().plusDays(30));
            issuedCouponRepository.save(issuedCoupon);
            return IssuedCouponResult.of(issuedCoupon);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("이미 발급된 쿠폰입니다.");
        }
    }

    public Coupon getCouponWithLock(Long couponId) {
        return couponRepository.findByIdForUpdate(couponId)
                .orElseThrow(() -> new EntityNotFoundException("해당 쿠폰을 찾을 수 없습니다."));
    }

    public Page<IssuedCouponResult> getIssuedCouponsByUserId(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "issuedAt")); // 최신 발급순 정렬

        Page<IssuedCoupon> issuedCouponPage = issuedCouponRepository.findAllByUserId(userId, pageable);
        return issuedCouponPage.map(IssuedCouponResult::of);
    }
}
