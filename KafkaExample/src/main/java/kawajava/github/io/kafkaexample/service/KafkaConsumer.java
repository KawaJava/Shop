package kawajava.github.io.kafkaexample.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "user-registration-topic", groupId = "user-registration-group")
    public void listen(String message) {
        System.out.println("Otrzymano wiadomość: " + message);
    }
}
