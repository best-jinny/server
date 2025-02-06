package kr.hhplus.be.server.point;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.point.domain.Point;
import kr.hhplus.be.server.point.domain.PointRepository;
import kr.hhplus.be.server.point.interfaces.controller.ChargeRequest;
import kr.hhplus.be.server.point.interfaces.controller.ChargeResponse;
import kr.hhplus.be.server.point.interfaces.controller.GetBalanceResponse;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.UserRepository;
import org.junit.jupiter.api.AfterEach;
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
public class PointIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointRepository pointRepository;

    @AfterEach
    void tearDown() {
        pointRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("유효한 amount 로 포인트 충전을 요청하면 요청한 금액만큼 충전된 잔액을 반환한다")
    public void chargeTest() throws Exception {
        // given
        Long beforePoint = 10000L;
        Long chargePoint = 50000L;
        Long expectedAfterPoint = 60000L;

        // 유저
        User user = User.builder()
                .name("moomin").build();
        userRepository.save(user);

        Point point = Point.builder()
                .point(beforePoint)
                .userId(user.getId())
                .build();

        pointRepository.save(point);

        ChargeRequest chargeRequest = new ChargeRequest(user.getId(), chargePoint);

        MvcResult result = mockMvc.perform(post("/api/v1/points")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(chargeRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ChargeResponse chargeResponse = objectMapper.readValue(response, ChargeResponse.class);

        assertThat(chargeResponse.getPoint()).isEqualTo(expectedAfterPoint);
    }

    @Test
    @DisplayName("0 포인트 충전 요청시 400 Bad Request 를 반환한다 ")
    public void chargeTest2() throws Exception {
        // given
        Long beforePoint = 10000L;
        Long chargePoint = 0L;

        // 유저
        User user = User.builder()
                .name("moomin").build();
        userRepository.save(user);

        Point point = Point.builder()
                .point(beforePoint)
                .userId(user.getId())
                .build();

        pointRepository.save(point);

        ChargeRequest chargeRequest = new ChargeRequest(user.getId(), chargePoint);

        MvcResult result = mockMvc.perform(post("/api/v1/points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(chargeRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("음수 포인트 충전 요청시 400 Bad Request 를 반환한다 ")
    public void chargeTest3() throws Exception {
        // given
        Long beforePoint = 10000L;
        Long chargePoint = -1000L;

        // 유저
        User user = User.builder()
                .name("moomin").build();
        userRepository.save(user);

        Point point = Point.builder()
                .point(beforePoint)
                .userId(user.getId())
                .build();

        pointRepository.save(point);

        ChargeRequest chargeRequest = new ChargeRequest(user.getId(), chargePoint);

        MvcResult result = mockMvc.perform(post("/api/v1/points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(chargeRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    @DisplayName("잔액 조회 요청시 해당 유저의 포인트 잔고를 반환한다.")
    public void chargeTest4() throws Exception {
        // given
        Long expectedPoint = 10000L;

        // 유저
        User user = User.builder()
                .name("moomin").build();
        userRepository.save(user);

        Point point = Point.builder()
                .point(expectedPoint)
                .userId(user.getId())
                .build();

        pointRepository.save(point);

        MvcResult result = mockMvc.perform(get("/api/v1/points/balance/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        GetBalanceResponse getBalanceResponse = objectMapper.readValue(response, GetBalanceResponse.class);

        assertThat(getBalanceResponse.getBalance()).isEqualTo(expectedPoint);
    }

}
