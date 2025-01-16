package kr.hhplus.be.server.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.common.entity.BaseTimeEntity;
import kr.hhplus.be.server.common.exceptions.ConflictException;
import kr.hhplus.be.server.common.exceptions.InvalidException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long price;

    @Builder
    public Product(String name, Long price) {
        this.name = name;
        this.price = price;
    }

    public void validatePrice(Long price) {
        if(!this.price.equals(price)) {
            throw new ConflictException("상품 가격이 변경되었습니다.");
        }
    }

}
