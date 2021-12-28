package com.venturedive.app.ticketing.core.processor;

import com.venturedive.app.ticketing.config.AppConfig;
import com.venturedive.app.ticketing.core.rule.RulesEngineProvider;
import com.venturedive.app.ticketing.core.rule.TicketGenerationForPickedupOrderRule;
import com.venturedive.app.ticketing.core.rule.TicketGenerationForPreparingOrderRule;
import com.venturedive.app.ticketing.core.rule.TicketGenerationForReceivedOrderRule;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TicketScheduler {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private TicketGenerationForReceivedOrderRule ticketGenerationForReceivedOrderRule;

    @Autowired
    private TicketGenerationForPreparingOrderRule ticketGenerationForPreparingOrderRule;

    @Autowired
    private TicketGenerationForPickedupOrderRule ticketGenerationForPickedupOrderRule;

    @Bean
    public String getCronValue()
    {
        String cronExpression = appConfig.getScheduleCronExpression();
        return cronExpression;
    }

    @Scheduled(cron = "#{@getCronValue}")
    public void fireRulesForTicketsGeneration(){
        Rules rules = new Rules();

        rules.register(ticketGenerationForReceivedOrderRule);
        rules.register(ticketGenerationForPreparingOrderRule);
        rules.register(ticketGenerationForPickedupOrderRule);

        Facts facts = new Facts();
        facts.put("overduePickedupDeliveriesExist", true);
        facts.put("overduePreparingDeliveriesExist", true);
        facts.put("overdueReceivedDeliveriesExist", true);

        RulesEngine rulesEngine = RulesEngineProvider.getInstance();
        rulesEngine.fire(rules, facts);
    }
}
