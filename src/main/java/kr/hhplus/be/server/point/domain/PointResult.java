package kr.hhplus.be.server.point.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PointResult {
    private Long point;

    public static PointResult of(Point point) {
        return new PointResult(
                point.getPoint()
        );
    }
}
