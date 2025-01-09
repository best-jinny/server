package kr.hhplus.be.server.order.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.entity.BaseTimeEntity;
import kr.hhplus.be.server.user.domain.User;
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

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    private Long totalPrice;
    private Long discountPrice;
    private Long finalPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ElementCollection
    @CollectionTable(name="order_line", joinColumns = @JoinColumn(name="order_id"))
    private List<OrderLine> orderLines;

    public static Order createOrder(User user, List<OrderLine> orderLines) {
        Order order = new Order();
        order.user = user;
        order.orderLines = orderLines;
        order.status = OrderStatus.PENDING;
        order.totalPrice = order.calculateTotalPrice();
        order.discountPrice = 0L;
        order.finalPrice = order.totalPrice;
        return order;
    }

    public Long calculateTotalPrice() {
        return orderLines.stream()
                .mapToLong(OrderLine::getPrice)
                .sum();
    }

    public void applyDiscount(Long discountPrice) {
        this.discountPrice = discountPrice;
        this.finalPrice = this.totalPrice - discountPrice;
    }

    public void complete() {
       this.status = OrderStatus.PAID;
    }

}
