package com.venturedive.app.ticketing.core.rule;

import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;

public final class RulesEngineProvider {
    private static RulesEngine instance = new DefaultRulesEngine();

    private RulesEngineProvider() {
    }

    public static RulesEngine getInstance() {
        if (instance == null) {
            instance = new DefaultRulesEngine();
        }
        return instance;
    }
}
