package com.aiinterview.payment.config;

import com.aiinterview.common.event.PaymentCompletedEvent;
import java.util.HashMap;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaConfig {
    @Bean ProducerFactory<String, PaymentCompletedEvent> producerFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrap) {
        var props = new HashMap<String, Object>(); props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap); props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); return new DefaultKafkaProducerFactory<>(props);
    }
    @Bean KafkaTemplate<String, PaymentCompletedEvent> paymentKafkaTemplate(ProducerFactory<String, PaymentCompletedEvent> pf) { return new KafkaTemplate<>(pf); }
}
