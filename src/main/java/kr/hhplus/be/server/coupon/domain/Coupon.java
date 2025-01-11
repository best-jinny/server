package kr.hhplus.be.server.coupon.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.entity.BaseTimeEntity;
import kr.hhplus.be.server.common.exceptions.InvalidCouponException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Coupon extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private int discountValue;
    private int maxIssuedCount;
    private int issuedCount;

    public Long calculateDiscountAmount(Long orderAmount) {
        return switch (discountType) {
            case FIXED -> Math.min(orderAmount, discountValue);
            case PERCENTAGE -> (orderAmount * discountValue) / 100;
        };
    }

    @Builder
    public Coupon(String name, DiscountType discountType, int discountValue, int maxIssuedCount) {
        this.name = name;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.maxIssuedCount = maxIssuedCount;
        this.issuedCount = 0;
    }

}
