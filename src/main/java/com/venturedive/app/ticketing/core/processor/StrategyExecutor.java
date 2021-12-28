package com.venturedive.app.ticketing.core.processor;

import com.venturedive.app.ticketing.common.enumerations.StrategyName;
import com.venturedive.app.ticketing.entity.Delivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StrategyExecutor {

    @Autowired
    private StrategyFactory strategyFactory;

    public StrategyExecutor(){
    }

    public void process(List<Delivery> list, StrategyName strategyName){
        Strategy receivedOrderStrategy = strategyFactory.findStrategy(strategyName);
        receivedOrderStrategy.process(list);
    }
}
