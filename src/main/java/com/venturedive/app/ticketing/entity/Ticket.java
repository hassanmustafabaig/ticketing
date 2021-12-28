package com.venturedive.app.ticketing.entity;

import com.venturedive.app.ticketing.common.enumerations.Priority;
import com.venturedive.app.ticketing.common.enumerations.TicketStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ticket")
@Getter
@Setter
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "priority")
    private Priority priority;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private TicketStatus ticketStatus;

    public Ticket(Priority priority, Delivery delivery, TicketStatus ticketStatus) {
        this.priority = priority;
        this.delivery = delivery;
        this.ticketStatus = ticketStatus;
    }
}
