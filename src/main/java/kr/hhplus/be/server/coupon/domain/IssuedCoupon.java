package kr.hhplus.be.server.coupon.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.entity.BaseTimeEntity;
import kr.hhplus.be.server.common.exceptions.InvalidCouponException;
import kr.hhplus.be.server.user.domain.User;
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
        if (coupon.isIssueLimitExceeded()) {
            throw new IllegalStateException("선착순 발급 마감");
        }

        IssuedCoupon issuedCoupon = new IssuedCoupon();
        issuedCoupon.coupon = coupon;
        issuedCoupon.userId = userId;
        issuedCoupon.status = CouponStatus.VALID;
        issuedCoupon.issuedAt = LocalDateTime.now();
        issuedCoupon.expiredAt = expiredAt;

        coupon.increaseIssuedCount();
        return issuedCoupon;
    }

    public void validate() {
        if(LocalDateTime.now().isAfter(expiredAt)) {
            throw new InvalidCouponException("만료된 쿠폰입니다");
        }

        if(status == CouponStatus.USED) {
            throw new InvalidCouponException("이미 사용된 쿠폰입니다");
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
