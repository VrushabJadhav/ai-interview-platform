package com.aiinterview.interview.config;

import com.aiinterview.common.event.InterviewSubmittedEvent;
import java.util.HashMap;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaConfig {
    @Bean ProducerFactory<String, InterviewSubmittedEvent> producerFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrap) {
        var props = new HashMap<String, Object>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }
    @Bean KafkaTemplate<String, InterviewSubmittedEvent> interviewKafkaTemplate(ProducerFactory<String, InterviewSubmittedEvent> pf) { return new KafkaTemplate<>(pf); }
}
