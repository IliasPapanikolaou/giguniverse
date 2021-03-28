package com.unipi.giguniverse.repository;

import com.unipi.giguniverse.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


public interface TicketRepository extends JpaRepository<Ticket, String> {

    List<Ticket> findByTicketHolderEmail(String email);

    List<Ticket> findByReservationConcertConcertId(Integer concertId);

    List<Ticket> findByTicketBuyerUserId(Integer concertId);

//    List<Ticket> findByAttendant(Attendant ticketBuyer);
}
