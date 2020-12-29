package com.unipi.giguniverse.dto;

import com.unipi.giguniverse.model.Attendant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto {

    private UUID ticketId;
    private String ticketHolder;
    private String ticketHolderEmail;
//    private TicketType ticketType;
    private double price;
    //TODO add Attendant
//    private Attendant ticketBuyer;
    private String phone;
    //TODO add reservation
}
