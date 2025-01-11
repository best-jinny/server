package kr.hhplus.be.server.order.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Table(name = "`order`")
@Entity
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long totalPrice;
    private Long discountPrice;
    private Long finalPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ElementCollection
    @CollectionTable(name="order_line", joinColumns = @JoinColumn(name="order_id"))
    private List<OrderLine> orderLines;

    public static Order createOrder(Long userId, List<OrderLine> orderLines) {
        Order order = new Order();
        order.userId = userId;
        order.orderLines = orderLines;
        order.status = OrderStatus.PENDING;
        order.totalPrice = order.calculateTotalPrice();
        order.discountPrice = 0L;
        order.finalPrice = order.totalPrice;
        return order;
    }

    private Long calculateTotalPrice() {
        return orderLines.stream()
                .mapToLong(OrderLine::getPrice)
                .sum();
    }

    public void applyDiscount(Long discountPrice) {

        if (discountPrice > this.totalPrice) {
            discountPrice = this.totalPrice; // 할인 금액을 총 금액으로 제한
        }

        this.discountPrice = discountPrice;
        this.finalPrice = this.totalPrice - discountPrice;
    }

    public void complete() {
       this.status = OrderStatus.PAID;
    }

}
