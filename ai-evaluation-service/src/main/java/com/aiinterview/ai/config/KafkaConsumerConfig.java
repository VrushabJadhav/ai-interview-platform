package com.aiinterview.ai.config;

import com.aiinterview.common.Constants;
import com.aiinterview.common.event.InterviewSubmittedEvent;
import java.util.HashMap;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {
    @Bean ConsumerFactory<String, InterviewSubmittedEvent> consumerFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrap) {
        var props = new HashMap<String, Object>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.aiinterview.common.event");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(InterviewSubmittedEvent.class, false));
    }
    @Bean ConcurrentKafkaListenerContainerFactory<String, InterviewSubmittedEvent> kafkaListenerContainerFactory(ConsumerFactory<String, InterviewSubmittedEvent> cf, KafkaTemplate<Object, Object> template) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, InterviewSubmittedEvent>();
        factory.setConsumerFactory(cf);
        factory.setCommonErrorHandler(new DefaultErrorHandler((record, ex) -> template.send(Constants.Topics.INTERVIEW_SUBMITTED_DLT, record.key(), record.value()), new FixedBackOff(1000L, 3L)));
        return factory;
    }
    @Bean ProducerFactory<Object, Object> producerFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrap) {
        var props = new HashMap<String, Object>(); props.put("bootstrap.servers", bootstrap); props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer"); props.put("value.serializer", "org.springframework.kafka.support.serializer.JsonSerializer");
        return new DefaultKafkaProducerFactory<>(props);
    }
    @Bean KafkaTemplate<Object, Object> kafkaTemplate(ProducerFactory<Object, Object> pf) { return new KafkaTemplate<>(pf); }
}
