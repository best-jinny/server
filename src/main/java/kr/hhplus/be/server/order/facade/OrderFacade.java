package kr.hhplus.be.server.order.facade;

import kr.hhplus.be.server.coupon.domain.CouponService;
import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.OrderService;
import kr.hhplus.be.server.order.infra.DataPlatform;
import kr.hhplus.be.server.payment.domain.PaymentCommand;
import kr.hhplus.be.server.payment.domain.PaymentMethod;
import kr.hhplus.be.server.payment.domain.PaymentService;
import kr.hhplus.be.server.product.domain.ProductService;
import kr.hhplus.be.server.product.domain.ValidateProductPriceCommand;
import kr.hhplus.be.server.stock.domain.DeductStockCommand;
import kr.hhplus.be.server.stock.domain.StockService;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final UserService userService;
    private final ProductService productService;
    private final StockService stockService;
    private final CouponService couponService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final DataPlatform dataPlatform;

    // 즉시 결제 주문
    @Transactional
    public OrderResult processImmediatePayOrder(OrderCriteria orderCriteria) {

       validateProductPrices(orderCriteria);
       processStockDeduction(orderCriteria);
       User user = fetchUser(orderCriteria);
       Order order = createOrder(orderCriteria);
       applyCouponIfPresent(orderCriteria, order);
       processPayment(order, user);
       completeOrder(order);
       sendDataPlatform();

        return OrderResult.from(order);
    }

    private void validateProductPrices(OrderCriteria criteria) {
        productService.validateProductPrices(ValidateProductPriceCommand.of(criteria.getOrderLines()));
    }

    private void processStockDeduction(OrderCriteria criteria) {
        for(OrderCriteria.OrderLineCriteria line : criteria.getOrderLines()) {
            stockService.deductStock(DeductStockCommand.of(line));
        }
    }

    private User fetchUser(OrderCriteria criteria) {
        return userService.getUser(criteria.getUserId());
    }

    private Order createOrder(OrderCriteria criteria) {
        return Order.createOrder(criteria.getUserId(), criteria.toOrderLines());
    }

    private void applyCouponIfPresent(OrderCriteria criteria, Order order) {
        if(criteria.getCouponId() != null) { // 쿠폰 유효성 확인 - 할인 금액 계산 - 쿠폰 사용처리 - 주문에 할인가 적용
            Long discountAmount = couponService.calculateDiscountAmount(criteria.getCouponId(), order.getTotalPrice());
            order.applyDiscount(discountAmount);
            couponService.redeemCoupon(criteria.getCouponId());
        }
    }

    private void processPayment(Order order, User user) {
        paymentService.processPayment(new PaymentCommand(user.getId(), order.getId(), order.getFinalPrice(), PaymentMethod.POINT));
    }

    private void completeOrder(Order order) {
        orderService.completeOrder(order);
    }

    private void sendDataPlatform() {
        dataPlatform.send();
    }
}
