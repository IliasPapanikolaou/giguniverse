package com.unipi.giguniverse.service;

import com.unipi.giguniverse.dto.TicketDto;
import com.unipi.giguniverse.exceptions.ApplicationException;
import com.unipi.giguniverse.model.Ticket;
import com.unipi.giguniverse.repository.TicketRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.pool.TypePool;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;

    private TicketDto mapTicketToDto(Ticket ticket){
        return TicketDto.builder()
                .ticketId(ticket.getTicketId())
                .ticketHolder(ticket.getTicketHolder())
                .ticketHolderEmail(ticket.getTicketHolderEmail())
                .price(ticket.getPrice())
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
        ticketRepository.save(mapTicketDto(ticketDto));
        return ticketDto;
    }

    public TicketDto getTicketById(Integer id){
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

/*    public List<TicketDto> getTicketsByAttendant(Attendant ticketBuyer){
        List<TicketDto> tickets = ticketRepository.findByAttendant(ticketBuyer)
                .stream()
                .map(this::mapTicketToDto)
                .collect(Collectors.toList());
        return tickets;
    }*/
}
