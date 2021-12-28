package com.venturedive.app.ticketing.core.rule;

import org.jeasy.rules.api.RulesEngine;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RulesEngineProviderTest {

    @Test
    void shouldReturnInstance() {
        RulesEngine result = RulesEngineProvider.getInstance();
        assertThat(result).isNotNull();
    }
}
