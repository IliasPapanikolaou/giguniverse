package com.unipi.giguniverse.repository;

import com.unipi.giguniverse.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    List<Ticket> findByTicketHolderEmail(String email);
//    List<Ticket> findByAttendant(Attendant ticketBuyer);
}
