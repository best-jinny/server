package kr.hhplus.be.server.stock.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.entity.BaseTimeEntity;
import kr.hhplus.be.server.common.exceptions.InvalidException;
import kr.hhplus.be.server.common.exceptions.NotEnoughException;
import kr.hhplus.be.server.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Stock extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private int quantity;

    @Builder
    public Stock(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void deduct(int amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("유효하지 않은 수량");
        }
        if(this.quantity - amount < 0) {
            throw new NotEnoughException("재고가 부족합니다");
        }
        this.quantity -= amount;
    }
}
