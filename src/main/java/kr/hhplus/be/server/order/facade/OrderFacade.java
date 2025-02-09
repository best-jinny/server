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
    public OrderResult processImmediatePayOrder(OrderCriteria criteria) {

        productService.validateProductPrices(ValidateProductPriceCommand.of(criteria.getOrderLines()));
        stockService.deductStocks(DeductStockCommand.fromOrderLines(criteria.getOrderLines()));
        User user = userService.getUser(criteria.getUserId());
        Order order = Order.createOrder(criteria.getUserId(), criteria.toOrderLines());

        Long discountAmount = couponService.calculateDiscountAmount(criteria.getCouponId(), order.getTotalPrice());
        order.applyDiscount(discountAmount);
        couponService.redeemCoupon(criteria.getCouponId());

        paymentService.processPayment(new PaymentCommand(user.getId(), order.getId(), order.getFinalPrice(), PaymentMethod.POINT));
        orderService.completeOrder(order);
        dataPlatform.send();

        return OrderResult.from(order);
    }

}
