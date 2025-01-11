package kr.hhplus.be.server.point.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.common.entity.BaseTimeEntity;
import kr.hhplus.be.server.common.exceptions.NotEnoughException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Point extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long point;

    @Builder
    public Point(Long userId, Long point) {
        this.userId = userId;
        this.point = point;
    }

    public void deduct(Long amount) {
        if(amount < 0) {
            throw new IllegalArgumentException();
        }
        if(this.point < amount) {
            throw new NotEnoughException("포인트가 부족합니다");
        }
        this.point -= amount;
    }
}
