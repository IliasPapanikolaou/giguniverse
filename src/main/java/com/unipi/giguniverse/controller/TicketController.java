package com.unipi.giguniverse.controller;

import com.unipi.giguniverse.dto.TicketDto;
import com.unipi.giguniverse.service.TicketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/ticket")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketDto> addTicket(@RequestBody TicketDto ticketDto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ticketService.addTicket(ticketDto));
    }

    @GetMapping
    public ResponseEntity<List<TicketDto>> getAllTickets(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Integer id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ticketService.getTicketById(id));
    }

    @GetMapping("/ticketHolderEmail/{ticketHolderEmail}")
    public ResponseEntity<List<TicketDto>> getTicketByEmail(@PathVariable String ticketHolderEmail){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ticketService.getTicketsByTicketHolderEmail(ticketHolderEmail));
    }

  /*  @GetMapping("/attendants/{attendant}")
    public ResponseEntity<List<TicketDto>> getTicketByAttendant(@PathVariable Attendant attendant){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ticketService.getTicketsByAttendant(attendant));
    }*/
}
