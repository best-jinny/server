package kr.hhplus.be.server.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.coupon.domain.*;
import kr.hhplus.be.server.order.domain.OrderStatus;
import kr.hhplus.be.server.order.interfaces.controller.OrderRequest;
import kr.hhplus.be.server.order.interfaces.controller.OrderResponse;
import kr.hhplus.be.server.point.domain.Point;
import kr.hhplus.be.server.point.domain.PointRepository;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.ProductRepository;
import kr.hhplus.be.server.stock.domain.Stock;
import kr.hhplus.be.server.stock.domain.StockRepository;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private IssuedCouponRepository issuedCouponRepository;

    @Test
    @DisplayName("즉시 결제 주문을 성공적으로 처리한다 - 할인 쿠폰 없음")
    public void testImmediatePayOrderSuccess() throws Exception {
        // given
        // 유저
        User user = User.builder()
                .name("moomin").build();
        userRepository.save(user);
        // 포인트
        Point point = Point.builder()
                .userId(user.getId())
                .point(100000L)
                .build();
        pointRepository.save(point);
        // 상품
        Product apple = Product.builder()
                .price(10000L)
                .name("apple")
                .build();
        productRepository.save(apple);

        Product mango = Product.builder()
                .price(30000L)
                .name("mango")
                .build();
        productRepository.save(mango);

        // 재고
        Stock appleStock = Stock.builder()
                .productId(apple.getId())
                .quantity(10)
                .build();
        stockRepository.save(appleStock);

        Stock mangoStock = Stock.builder()
                .productId(mango.getId())
                .quantity(5)
                .build();
        stockRepository.save(mangoStock);


        List<OrderRequest.OrderLineRequest> orderLineRequests = List.of(
                new OrderRequest.OrderLineRequest(apple.getId(), 1, 10000L),
                new OrderRequest.OrderLineRequest(mango.getId(), 1, 30000L)
        );

        OrderRequest orderRequest = new OrderRequest(user.getId(), orderLineRequests);

        MvcResult result = mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        OrderResponse orderResponse = objectMapper.readValue(response, OrderResponse.class);
        assertThat(orderResponse.getStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(orderResponse.getTotalPrice()).isEqualTo(40000L);
        assertThat(orderResponse.getDiscountPrice()).isEqualTo(0);
        assertThat(orderResponse.getFinalPrice()).isEqualTo(40000L);

    }

    @Test
    @DisplayName("5000원 할인 쿠폰을 적용하여 즉시 결제 주문을 성공한다")
    public void testImmediatePayOrderSuccessWithCoupon() throws Exception {
        // given
        // 유저
        User user = User.builder()
                .name("moomin").build();
        userRepository.save(user);
        // 포인트
        Point point = Point.builder()
                .userId(user.getId())
                .point(100000L)
                .build();
        pointRepository.save(point);
        // 상품
        Product apple = Product.builder()
                .price(10000L)
                .name("apple")
                .build();
        productRepository.save(apple);

        Product mango = Product.builder()
                .price(30000L)
                .name("mango")
                .build();
        productRepository.save(mango);

        // 재고
        Stock appleStock = Stock.builder()
                .productId(apple.getId())
                .quantity(10)
                .build();
        stockRepository.save(appleStock);

        Stock mangoStock = Stock.builder()
                .productId(mango.getId())
                .quantity(5)
                .build();
        stockRepository.save(mangoStock);
        // 쿠폰
        Coupon luckyCoupon = Coupon.builder()
                .name("luckyCoupon")
                .issuedCount(100)
                .discountType(DiscountType.FIXED)
                .discountValue(5000)
                .build();
        couponRepository.save(luckyCoupon);
        // 발급된 쿠폰
        IssuedCoupon issuedCoupon = IssuedCoupon.builder()
                .status(CouponStatus.VALID)
                .userId(user.getId())
                .coupon(luckyCoupon)
                .issuedAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusDays(10))
                .build();
        issuedCouponRepository.save(issuedCoupon);
        List<OrderRequest.OrderLineRequest> orderLineRequests = List.of(
                new OrderRequest.OrderLineRequest(apple.getId(), 1, 10000L),
                new OrderRequest.OrderLineRequest(mango.getId(), 1, 30000L)
        );

        OrderRequest orderRequest = new OrderRequest(user.getId(), orderLineRequests, issuedCoupon.getId());

        MvcResult result = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        OrderResponse orderResponse = objectMapper.readValue(response, OrderResponse.class);
        assertThat(orderResponse.getStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(orderResponse.getTotalPrice()).isEqualTo(40000L);
        assertThat(orderResponse.getDiscountPrice()).isEqualTo(5000L);
        assertThat(orderResponse.getFinalPrice()).isEqualTo(35000L);
    }

    @Test
    @DisplayName("재고가 0 인 상품을 주문하면 409 Conflict 를 반환한다")
    public void testImmediatePayOrderFail() throws Exception {
        // given
        // 유저
        User user = User.builder()
                .name("moomin").build();
        userRepository.save(user);
        // 포인트
        Point point = Point.builder()
                .userId(user.getId())
                .point(100000L)
                .build();
        pointRepository.save(point);
        // 상품
        Product apple = Product.builder()
                .price(10000L)
                .name("apple")
                .build();
        productRepository.save(apple);

        // 재고
        Stock appleStock = Stock.builder()
                .productId(apple.getId())
                .quantity(0)
                .build();
        stockRepository.save(appleStock);


        List<OrderRequest.OrderLineRequest> orderLineRequests = List.of(
                new OrderRequest.OrderLineRequest(apple.getId(), 1, 10000L)
        );

        OrderRequest orderRequest = new OrderRequest(user.getId(), orderLineRequests);

        MvcResult result = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Test
    @DisplayName("포인트 잔고가 부족한 상황에서 상품을 주문하면 409 Conflict 를 반환한다")
    public void testImmediatePayOrderFail2() throws Exception {
        // given
        // 유저
        User user = User.builder()
                .name("moomin").build();
        userRepository.save(user);
        // 포인트
        Point point = Point.builder()
                .userId(user.getId())
                .point(5000L)
                .build();
        pointRepository.save(point);
        // 상품
        Product apple = Product.builder()
                .price(10000L)
                .name("apple")
                .build();
        productRepository.save(apple);

        // 재고
        Stock appleStock = Stock.builder()
                .productId(apple.getId())
                .quantity(10)
                .build();
        stockRepository.save(appleStock);


        List<OrderRequest.OrderLineRequest> orderLineRequests = List.of(
                new OrderRequest.OrderLineRequest(apple.getId(), 1, 10000L)
        );

        OrderRequest orderRequest = new OrderRequest(user.getId(), orderLineRequests);

        MvcResult result = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isConflict())
                .andReturn();
    }
}
