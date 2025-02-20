package kr.hhplus.be.server.common.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    @KafkaListener(topics = "test-topic", groupId = "test-group")
    public void listen(String message) {
        messageQueue.offer(message);
    }

    public String pollMessage() throws InterruptedException {
        return messageQueue.poll();
    }

}
