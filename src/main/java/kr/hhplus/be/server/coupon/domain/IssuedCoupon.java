package kr.hhplus.be.server.coupon.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.entity.BaseTimeEntity;
import kr.hhplus.be.server.common.exceptions.ConflictException;
import kr.hhplus.be.server.common.exceptions.InvalidException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table(
        name = "issued_coupon",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"coupon_id", "user_id"})
        }
)
@Entity
public class IssuedCoupon extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "coupon_id")
    @ManyToOne
    private Coupon coupon;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private CouponStatus status; // VALID, USED
    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt;

    public static IssuedCoupon issue(Coupon coupon, Long userId, LocalDateTime expiredAt) {
        coupon.validateIssueLimit();
        coupon.increaseIssuedCount();

        IssuedCoupon issuedCoupon = new IssuedCoupon();
        issuedCoupon.coupon = coupon;
        issuedCoupon.userId = userId;
        issuedCoupon.status = CouponStatus.VALID;
        issuedCoupon.issuedAt = LocalDateTime.now();
        issuedCoupon.expiredAt = expiredAt;

        return issuedCoupon;
    }

    public void validate() {
        validateExpiry();
        validateUsage();
    }

    private void validateExpiry() {
        if (LocalDateTime.now().isAfter(expiredAt)) {
            throw new InvalidException("만료된 쿠폰입니다.");
        }
    }

    private void validateUsage() {
        if (status == CouponStatus.USED) {
            throw new ConflictException("이미 사용된 쿠폰입니다.");
        }
    }

    public void markAsUsed() {
       validate();
       status = CouponStatus.USED;
    }

    @Builder
    public IssuedCoupon(Coupon coupon, Long userId, CouponStatus status, LocalDateTime issuedAt, LocalDateTime expiredAt) {
        this.coupon = coupon;
        this.userId = userId;
        this.status = status;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
    }


}
