package com.venturedive.app.ticketing.core.processor;

import com.venturedive.app.ticketing.common.enumerations.StrategyName;
import com.venturedive.app.ticketing.entity.Delivery;

import java.util.List;

public interface Strategy {
    void process(List<Delivery> list);
    StrategyName getStrategyName();
}
