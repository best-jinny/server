package kr.hhplus.be.server.point.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.entity.BaseTimeEntity;
import kr.hhplus.be.server.common.exceptions.InvalidException;
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

    @Version
    private int version; // 낙관적 락을 위한 버전

    @Builder
    public Point(Long userId, Long point) {
        this.userId = userId;
        this.point = point;
    }

    public void deduct(Long amount) {
        if(amount < 0) {
            throw new InvalidException("포인트는 음수일 수 없습니다.");
        }
        if(this.point < amount) {
            throw new NotEnoughException("포인트가 부족합니다");
        }
        this.point -= amount;
    }

    public void charge(Long amount) {
        this.point += amount;
    }
}
