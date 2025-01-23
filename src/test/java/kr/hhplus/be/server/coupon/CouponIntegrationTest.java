package kr.hhplus.be.server.coupon;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.common.dto.PageResponse;
import kr.hhplus.be.server.coupon.domain.*;
import kr.hhplus.be.server.coupon.interfaces.controller.CouponIssueRequest;
import kr.hhplus.be.server.coupon.interfaces.controller.CouponIssueResponse;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class CouponIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private IssuedCouponRepository issuedCouponRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("잔여 수량이 남은 쿠폰 발급 첫 요청 시 쿠폰 발급에 성공한다")
    public void test() throws Exception {
        // given
        // 유저
        User user = User.builder()
                .name("moomin").build();
        userRepository.save(user);

        // 쿠폰
        Coupon luckyCoupon = Coupon.builder()
                .name("luckyCoupon")
                .issueLimit(100)
                .issuedCount(0)
                .discountType(DiscountType.FIXED)
                .discountValue(5000)
                .build();
        couponRepository.save(luckyCoupon);

        CouponIssueRequest couponIssueRequest = new CouponIssueRequest(luckyCoupon.getId(), user.getId());

        MvcResult result = mockMvc.perform(post("/api/v1/coupons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(couponIssueRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        CouponIssueResponse couponIssueResponse = objectMapper.readValue(response, CouponIssueResponse.class);

        assertThat(couponIssueResponse.getCouponName()).isEqualTo("luckyCoupon");
        assertThat(couponIssueResponse.getDiscountType()).isEqualTo(DiscountType.FIXED);
        assertThat(couponIssueResponse.getDiscountValue()).isEqualTo(5000);
        assertThat(couponIssueResponse.getStatus()).isEqualTo(CouponStatus.VALID);
    }

    @Test
    @DisplayName("선착순 발급 마감된 쿠폰 발급 요청 시 409 Conflict 를 반환한다")
    public void test2() throws Exception {
        // given
        // 유저
        User user = User.builder()
                .name("moomin").build();
        userRepository.save(user);

        // 쿠폰
        Coupon luckyCoupon = Coupon.builder()
                .name("luckyCoupon")
                .issueLimit(100)
                .issuedCount(100)
                .build();
        couponRepository.save(luckyCoupon);

        CouponIssueRequest couponIssueRequest = new CouponIssueRequest(luckyCoupon.getId(), user.getId());

        MvcResult result = mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(couponIssueRequest)))
                .andExpect(status().isConflict())
                .andReturn();

    }

    @Test
    @DisplayName("이미 발급받은 쿠폰을 중복 발급 요청 시 400 Bad Request 를 반환한다")
    public void test3() throws Exception {
        // given
        // 유저
        User user = User.builder()
                .name("moomin").build();
        userRepository.save(user);

        // 쿠폰
        Coupon luckyCoupon = Coupon.builder()
                .name("luckyCoupon")
                .issueLimit(100)
                .issuedCount(10)
                .build();
        couponRepository.save(luckyCoupon);

        // 쿠폰 발급
        IssuedCoupon issuedCoupon = IssuedCoupon.builder()
                .userId(user.getId())
                .coupon(luckyCoupon)
                .build();
        issuedCouponRepository.save(issuedCoupon);

        CouponIssueRequest couponIssueRequest = new CouponIssueRequest(luckyCoupon.getId(), user.getId());

        MvcResult result = mockMvc.perform(post("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(couponIssueRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("발급 받은 쿠폰 조회 요청 시 유효한 쿠폰 리스트를 반환한다")
    public void test4() throws Exception {
        // given
        // 유저
        User user = User.builder()
                .name("moomin").build();
        userRepository.save(user);
        // 쿠폰
        Coupon luckyCoupon = Coupon.builder()
                .name("luckyCoupon")
                .issueLimit(100)
                .issuedCount(10)
                .build();
        couponRepository.save(luckyCoupon);

        Coupon happyCoupon = Coupon.builder()
                .name("happyCoupon")
                .issueLimit(100)
                .issuedCount(10)
                .build();
        couponRepository.save(happyCoupon);

        // 쿠폰 발급
        IssuedCoupon issuedLuckyCoupon = IssuedCoupon.builder()
                .userId(user.getId())
                .coupon(luckyCoupon)
                .status(CouponStatus.VALID)
                .build();
        issuedCouponRepository.save(issuedLuckyCoupon);

        IssuedCoupon issuedHappyCoupon = IssuedCoupon.builder()
                .userId(user.getId())
                .coupon(happyCoupon)
                .status(CouponStatus.VALID)
                .build();
        issuedCouponRepository.save(issuedHappyCoupon);

        MvcResult result = mockMvc.perform(get("/api/v1/coupons/user/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        PageResponse<IssuedCouponResult> response = objectMapper.readValue(responseBody,
                new TypeReference<PageResponse<IssuedCouponResult>>() {});

        // 페이지 검증
        assertThat(response.getContent().size()).isEqualTo(2);
        assertThat(response.getTotalElements()).isEqualTo(2);
        assertThat(response.getTotalPages()).isEqualTo(1);
        assertThat(response.getSize()).isEqualTo(10);

        // 개별 쿠폰 데이터 검증
        IssuedCouponResult luckyCouponResult = response.getContent().get(0);
        assertThat(luckyCouponResult.getCouponName()).isEqualTo("luckyCoupon");
        assertThat(luckyCouponResult.getStatus()).isEqualTo(CouponStatus.VALID);

        IssuedCouponResult happyCouponResult = response.getContent().get(1);
        assertThat(happyCouponResult.getCouponName()).isEqualTo("happyCoupon");
        assertThat(happyCouponResult.getStatus()).isEqualTo(CouponStatus.VALID);
    }
}
