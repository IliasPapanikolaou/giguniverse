package com.unipi.giguniverse.service;

import com.unipi.giguniverse.model.Ticket;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    String build(String message){
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("mailTemplate", context);
    }

    String buildTicketMail(Ticket ticket){
        Context context = new Context();
        context.setVariable("name", ticket.getTicketHolder());
        context.setVariable("email", ticket.getTicketHolderEmail());
        context.setVariable("price", ticket.getPrice());
        context.setVariable("purchaseDate", ticket.getPurchaseDate());
        context.setVariable("phone", ticket.getPhone());
        context.setVariable("concert", ticket.getReservation().getConcert().getConcertName());
        context.setVariable("concertDate", ticket.getReservation().getFinalDate());
        return templateEngine.process("ticketTemplate", context);
    }
}
