package com.unipi.giguniverse.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Ticket {

    @Id
    private UUID ticketId;
    private String ticketHolder;
    private String getTicketHolderEmail;
    @OneToOne(fetch = LAZY)
    private Reservation reservation;
    private TicketType ticketType;
    private double price;
}

enum TicketType{
    STADIUM_ZONE_1,
    STADIUM_ZONE_2,
    STADIUM_TIER_1,
    STADIUM_TIER_2,
    STADIUM_VIP,
    CLUB_BAR,
    CLUB_TABLE,
    CLUB_STANDING
}
