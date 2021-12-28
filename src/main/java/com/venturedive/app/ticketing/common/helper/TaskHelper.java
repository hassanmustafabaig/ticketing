package com.venturedive.app.ticketing.common.helper;

import com.venturedive.app.ticketing.common.enumerations.CustomerType;
import com.venturedive.app.ticketing.common.enumerations.Priority;
import com.venturedive.app.ticketing.common.enumerations.StrategyName;
import com.venturedive.app.ticketing.core.processor.Strategy;
import com.venturedive.app.ticketing.dto.TicketDto;
import com.venturedive.app.ticketing.entity.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class TaskHelper {
    private Map<CustomerType, Priority> priorities;

    @Autowired
    public TaskHelper(Set<CustomerType> prioritySet) {
        populate();
    }

    public Priority findPriorityByCustomerType(CustomerType customerType) {
        return priorities.get(customerType);
    }

    private void populate() {
        priorities = new HashMap<CustomerType, Priority>();
        priorities.put(CustomerType.VIP, Priority.HIGHEST);
        priorities.put(CustomerType.LOYAL, Priority.HIGH);
        priorities.put(CustomerType.NEW, Priority.MEDIUM);
    }
}



