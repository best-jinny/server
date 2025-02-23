package kr.hhplus.be.server.order;

import kr.hhplus.be.server.common.kafka.KafkaConsumerService;
import kr.hhplus.be.server.common.kafka.KafkaProducerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Testcontainers
public class KafkaIntegrationTest {

    @Autowired
    private KafkaProducerService producerService;

    @Autowired
    private KafkaConsumerService consumerService;

    @Test
    @DisplayName("kafka 연동 테스트")
    public void testKafkaProducerAndConsumerService() throws InterruptedException {
        String testMessage = "Hello Kafka";
        producerService.sendMessage("test-topic", testMessage);

        Thread.sleep(2000);
        String receivedMessage = consumerService.pollMessage();

        assertThat(receivedMessage).isEqualTo(testMessage);
    }


}
