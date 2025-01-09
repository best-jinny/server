package kr.hhplus.be.server.coupon.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.entity.BaseTimeEntity;
import kr.hhplus.be.server.common.exceptions.InvalidCouponException;
import kr.hhplus.be.server.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class IssuedCoupon extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "coupon_id")
    @ManyToOne
    private Coupon coupon;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private CouponStatus status; // VALID, USED
    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt;

    public void validate() {
        if(LocalDateTime.now().isAfter(expiredAt)) {
            throw new InvalidCouponException("만료된 쿠폰입니다");
        }

        if(status.equals(CouponStatus.USED)) {
            throw new InvalidCouponException("이미 사용된 쿠폰입니다");
        }
    }

    public void markAsUsed() {
       validate();
       status = CouponStatus.USED;
    }


}
