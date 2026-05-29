package com.aiinterview.gateway.config;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class GatewayConfigTest {
    @Test void createsCorsFilter() { assertThat(new GatewayConfig().corsWebFilter()).isNotNull(); }
}
