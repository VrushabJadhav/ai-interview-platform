package com.aiinterview.notification.config;

import java.util.HashMap;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {
    @Bean ConsumerFactory<Object, Object> consumerFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrap) {
        var props = new HashMap<String, Object>(); props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap); props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class); props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.aiinterview.common.event");
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(ConsumerFactory<Object, Object> cf) { var factory = new ConcurrentKafkaListenerContainerFactory<Object, Object>(); factory.setConsumerFactory(cf); return factory; }
}
