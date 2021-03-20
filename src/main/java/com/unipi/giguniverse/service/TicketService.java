package com.unipi.giguniverse.service;

import com.unipi.giguniverse.dto.TicketDto;
import com.unipi.giguniverse.exceptions.ApplicationException;
import com.unipi.giguniverse.model.Concert;
import com.unipi.giguniverse.model.NotificationEmail;
import com.unipi.giguniverse.model.Reservation;
import com.unipi.giguniverse.model.Ticket;
import com.unipi.giguniverse.repository.ConcertRepository;
import com.unipi.giguniverse.repository.ReservationRepository;
import com.unipi.giguniverse.repository.TicketRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;
    private final ConcertRepository concertRepository;
    private final AuthService authService;
    private final MailService mailService;

    private TicketDto mapTicketToDto(Ticket ticket){
        return TicketDto.builder()
                .ticketId(ticket.getTicketId())
                .ticketHolder(ticket.getTicketHolder())
                .ticketHolderEmail(ticket.getTicketHolderEmail())
                .ticketBuyerId(authService.getCurrentUserDetails().getUserId())
                .concertId(ticket.getReservation().getConcert().getConcertId())
                .price(ticket.getPrice())
                .purchaseDate(ticket.getPurchaseDate())
                .phone(ticket.getPhone())
                .build();
    }

    private Ticket mapTicketDto(TicketDto ticketDto){
        return Ticket.builder()
                .ticketHolder(ticketDto.getTicketHolder())
                .ticketHolderEmail(ticketDto.getTicketHolderEmail())
                .price(ticketDto.getPrice())
                .phone(ticketDto.getPhone())
                .build();
    }

    public TicketDto addTicket(TicketDto ticketDto){
        Concert concert = concertRepository.getOne(ticketDto.getConcertId());
        int reservationId = Objects.requireNonNull(concert.getReservation()).getReservationId();
        Reservation reservation = reservationRepository.getOne(reservationId);
        Ticket ticket = mapTicketDto(ticketDto);
        ticket.setReservation(reservation);
        ticket.setTicketBuyer(authService.getCurrentUserDetails());
        String ticketId = ticketRepository.save(ticket).getTicketId();
        ticket.setPrice(reservation.getTicketPrice());
        ticket.setPurchaseDate(Date.from(Instant.now()));
        ticket.setTicketId(ticketId);
        //Reduce available tickets by one
        reduceAvailableTicketsByOne(reservation.getReservationId());
        //Send mail to ticket  holders
        sendEmailToTicketHolders(ticket);
        ticketDto = mapTicketToDto(ticket);
        return ticketDto;
    }

    public TicketDto getTicketById(String id){
        Optional<Ticket> ticket = ticketRepository.findById(id);
        TicketDto ticketDto=mapTicketToDto(ticket.orElseThrow(()->new ApplicationException("Ticket not found")));
        return ticketDto;
    }

    public List<TicketDto> getAllTickets(){
        List<TicketDto> tickets = ticketRepository.findAll()
                .stream()
                .map(this::mapTicketToDto)
                .collect(toList());
        return tickets;
    }

    public List<TicketDto> getTicketsByTicketHolderEmail(String ticketHolderEmail){
        List<TicketDto> tickets = ticketRepository.findByTicketHolderEmail(ticketHolderEmail)
                .stream()
                .map(this::mapTicketToDto)
                .collect(toList());
        return tickets;
    }

    public TicketDto updateTicket(TicketDto ticketDto){
        Ticket existingTicket = ticketRepository.getOne(ticketDto.getTicketId());
        existingTicket.setTicketHolder(ticketDto.getTicketHolder());
        existingTicket.setTicketHolderEmail(ticketDto.getTicketHolderEmail());
        existingTicket.setPrice(ticketDto.getPrice());
        existingTicket.setPhone(ticketDto.getPhone());
        ticketRepository.save(existingTicket);
        return mapTicketToDto(existingTicket);
    }

    public String deleteTicket(String ticketId) {
        ticketRepository.deleteById(ticketId);
        return "Ticket with id:" + ticketId.toString() + " was deleted.";
    }

    private Integer reduceAvailableTicketsByOne(Integer reservationId){
        Reservation reservation = reservationRepository.getOne(reservationId);
        reservation.setTicketNumber(reservation.getTicketNumber()-1);
        return reservation.getTicketNumber();
    }

    private void sendEmailToTicketHolders(Ticket ticket){
        mailService.sendMail(new NotificationEmail("Αγορά εισιτηρίου απο GigUniverse",
                ticket.getTicketHolderEmail(),
                "Αγοράσατε εισιτήριο απο εμάς... Καλά να πάθετε."
                        +"<br />Αριθμός εισιτηρίου:" +ticket.getTicketId()
                        +"<br />Όνομα δικαιούχου: " +ticket.getTicketHolder()
                        +"<br />Ημε/νία αγοράς: " +ticket.getPurchaseDate()
                        +"<br />Όνομα συγκροτήματος" +ticket.getReservation().getConcert().getConcertName()));
    }

/*    public List<TicketDto> getTicketsByAttendant(Attendant ticketBuyer){
        List<TicketDto> tickets = ticketRepository.findByAttendant(ticketBuyer)
                .stream()
                .map(this::mapTicketToDto)
                .collect(Collectors.toList());
        return tickets;
    }*/
}
