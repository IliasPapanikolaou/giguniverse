package com.unipi.giguniverse.service;

import com.unipi.giguniverse.dto.ConcertDto;
import com.unipi.giguniverse.dto.TicketDto;
import com.unipi.giguniverse.exceptions.ApplicationException;
import com.unipi.giguniverse.model.*;
import com.unipi.giguniverse.repository.ConcertRepository;
import com.unipi.giguniverse.repository.ReservationRepository;
import com.unipi.giguniverse.repository.TicketRepository;

import com.unipi.giguniverse.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
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
    private final UserRepository userRepository;
    private final AuthService authService;
    private final MailService mailService;
    private final QRGeneratorService qrGeneratorService;
    private final ConcertService concertService;

    private TicketDto mapTicketToDto(Ticket ticket){
        return TicketDto.builder()
                .ticketId(ticket.getTicketId())
                .ticketHolder(ticket.getTicketHolder())
                .ticketHolderEmail(ticket.getTicketHolderEmail())
                .ticketBuyerId(authService.getCurrentUserDetails().getUserId())
                .concertId(ticket.getReservation().getConcert().getConcertId())
                .concertDto(concertService.mapConcertToDto(ticket.getReservation().getConcert()))
                .price(ticket.getPrice())
                .purchaseDate(ticket.getPurchaseDate())
                .phone(ticket.getPhone())
                .qrcode(generateQRCodeImageToString(ticket))
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

//    public TicketDto addTicket(TicketDto ticketDto){
//        Concert concert = concertRepository.getOne(ticketDto.getConcertId());
//        int reservationId = Objects.requireNonNull(concert.getReservation()).getReservationId();
//        Reservation reservation = reservationRepository.getOne(reservationId);
//
//        //Buy Ticket if there is any
//        if (checkAvailabilityAndBuyTicket(reservation.getReservationId())){
//            Ticket ticket = mapTicketDto(ticketDto);
//            ticket.setReservation(reservation);
//            ticket.setTicketBuyer(authService.getCurrentUserDetails());
//            String ticketId = ticketRepository.save(ticket).getTicketId();
//            ticket.setPrice(reservation.getTicketPrice());
//            ticket.setPurchaseDate(Date.from(Instant.now()));
//            ticket.setTicketId(ticketId);
//            //Send mail to ticket holder
//            sendEmailToTicketHolders(ticket, generateQRCodeImageToString(ticket));
//            ticketDto = mapTicketToDto(ticket);
//            return ticketDto;
//        }
//        else throw new ApplicationException("Tickets are sold out");
//    }

    public List<TicketDto> addTickets(List<TicketDto> ticketDtos){

        Concert concert = concertRepository.getOne(ticketDtos.get(0).getConcertId());
        int reservationId = Objects.requireNonNull(concert.getReservation()).getReservationId();
        Reservation reservation = reservationRepository.getOne(reservationId);

        List<TicketDto> tickets = new ArrayList<>();
        for(TicketDto ticketDto: ticketDtos){
            //Buy Ticket if there is any
            if (checkAvailabilityAndBuyTicket(reservation.getReservationId())){
                Ticket ticket = mapTicketDto(ticketDto);
                ticket.setReservation(reservation);
                ticket.setTicketBuyer(authService.getCurrentUserDetails());
                String ticketId = ticketRepository.save(ticket).getTicketId();
                ticket.setPrice(reservation.getTicketPrice());
                ticket.setPurchaseDate(Date.from(Instant.now()));
                ticket.setTicketId(ticketId);
                //Send mail to ticket holder
                sendEmailToTicketHolders(ticket, generateQRCodeImageToString(ticket));
                ticketDto = mapTicketToDto(ticket);
                tickets.add(ticketDto);
            }
            else throw new ApplicationException("Tickets are sold out");
        }
        return tickets;
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

    public List<TicketDto> getTicketsByConcertID(Integer concertId){
        List<TicketDto> tickets = ticketRepository.findByReservationConcertConcertId(concertId)
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
    public List<TicketDto> getTicketsByLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        Optional<User> user =  userRepository.findByEmail(principal.getUsername());
        List<TicketDto> tickets = ticketRepository.findByTicketHolderEmail(user.get().getEmail())
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
        return "Ticket with id:" + ticketId + " was deleted.";
    }

    private boolean checkAvailabilityAndBuyTicket(Integer reservationId){
        Reservation reservation = reservationRepository.getOne(reservationId);
        if (reservation.getTicketNumber() > 0){
            //Reduce ticket count
            reservation.setTicketNumber(reservation.getTicketNumber()-1);
            return true;
        }
        else return false;
    }

    private void sendEmailToTicketHolders(Ticket ticket, String qrString){
        //generateQRCodeImage(ticket);
        mailService.sendTicketEMail(ticket, qrString);
    }

//    private void generateQRCodeImage(Ticket ticket){
//        String path = getClass().getResource("/templates/QRCode.png").getPath().substring(1);
//        String qrText = ticket.getTicketId() + "\n" +
//                ticket.getReservation().getConcert().getConcertName() + "\n" +
//                ticket.getTicketHolder() + "\n" +
//                ticket.getTicketHolderEmail();
//        qrGeneratorService.generateQRCodeImage(qrText,150, 150, path);
//    }

    private String generateQRCodeImageToString(Ticket ticket){
        String qrText = ticket.getTicketId() + "\n" +
                ticket.getReservation().getConcert().getConcertName() + "\n" +
                ticket.getTicketHolder() + "\n" +
                ticket.getTicketHolderEmail() + "\n" +
                ticket.getPurchaseDate();
        return qrGeneratorService.getQRCodeImageAsBase64(qrText,150, 150);
    }

/*    public List<TicketDto> getTicketsByAttendant(Attendant ticketBuyer){
        List<TicketDto> tickets = ticketRepository.findByAttendant(ticketBuyer)
                .stream()
                .map(this::mapTicketToDto)
                .collect(Collectors.toList());
        return tickets;
    }*/
}
