package com.unipi.giguniverse.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Ticket {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID ticketId;
    private String ticketHolder;
    private String ticketHolderEmail;
    @OneToOne(fetch = LAZY)
    private Reservation reservation;
    private TicketType ticketType;
    private double price;
    @OneToOne (fetch =LAZY)
    private Attendant ticketBuyer;
    private String phone;
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
