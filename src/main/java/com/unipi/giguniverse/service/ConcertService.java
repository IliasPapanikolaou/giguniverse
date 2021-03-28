package com.unipi.giguniverse.service;

import com.unipi.giguniverse.dto.ConcertDto;
import com.unipi.giguniverse.dto.VenueDto;
import com.unipi.giguniverse.exceptions.ApplicationException;
import com.unipi.giguniverse.model.*;

import com.unipi.giguniverse.repository.ConcertRepository;
import com.unipi.giguniverse.repository.ReservationRepository;
import com.unipi.giguniverse.repository.UserRepository;
import com.unipi.giguniverse.repository.VenueRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final VenueRepository venueRepository;
    private final VenueService venueService;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    ConcertDto mapConcertToDto(Concert concert){
        Reservation reservation = reservationRepository
                .getOne(Objects.requireNonNull(concert.getReservation()).getReservationId());
        return ConcertDto.builder()
                .concertId(concert.getConcertId())
                .concertName(concert.getConcertName())
                .description(concert.getDescription())
                .venueId(concert.getVenue().getVenueId())
                .venue(venueService.mapVenueToVenueDto(concert.getVenue()))
                .date(concert.getDate())
                .ticketNumber(reservation.getTicketNumber())
                .ticketPrice(reservation.getTicketPrice())
                .image(concert.getImage())
                .build();
    }
    private Concert mapConcertDto(ConcertDto concertDto){
        return Concert.builder()
                .concertName(concertDto.getConcertName())
                .description(concertDto.getDescription())
                .venue(venueRepository.getOne(concertDto.getVenueId()))
                .date(concertDto.getDate())
                .image(concertDto.getImage())
                .build();
    }
    public ConcertDto addConcert(ConcertDto concertDto){
        concertRepository.save(mapConcertDto(concertDto));
        return concertDto;
    }
    public ConcertDto getConcertById(Integer id){
        Optional<Concert> concert = concertRepository.findById(id);
        ConcertDto concertDto=mapConcertToDto(concert.orElseThrow(()->new ApplicationException("Concert not found")));
        return concertDto;
    }
    public List<ConcertDto> getAllConcerts(){
        List<ConcertDto> concerts = concertRepository.findAll()
                .stream()
                .map(this::mapConcertToDto)
                .collect(toList());
        return concerts;
    }
    public List<ConcertDto> getConcertByDate(LocalDate date){
        List<ConcertDto> concerts = concertRepository.findByDate(date)
                .stream()
                .map(this::mapConcertToDto)
                .collect(toList());
        return concerts;
    }
    public List<ConcertDto> getConcertByVenue(Venue venue){
        List<ConcertDto> concerts = concertRepository.findByVenue(venue)
                .stream()
                .map(this::mapConcertToDto)
                .collect(toList());
        return concerts;
    }
    public List<ConcertDto> getConcertByMonth(LocalDate date){
        LocalDate start = date.withDayOfMonth(1);
        LocalDate end = date.plusMonths(1).withDayOfMonth(1).minusDays(1);
        List<ConcertDto> concerts = concertRepository.findByDateGreaterThanAndDateLessThan(start,end)
                .stream()
                .map(this::mapConcertToDto)
                .collect(toList());
        return concerts;
    }

    public List<ConcertDto> getConcertByLoggedInOwner(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        Optional<User> owner =  userRepository.findByEmail(principal.getUsername());
        List<ConcertDto> concerts = concertRepository.findByVenueOwnerUserId(owner.get().getUserId())
                .stream()
                .map(this::mapConcertToDto)
                .collect(toList());
        return concerts;
    }

    public ConcertDto updateConcert(ConcertDto concertDto){
        Concert existingConcert = concertRepository.getOne(concertDto.getConcertId());
        Reservation reservation = reservationRepository
                .getOne(Objects.requireNonNull(existingConcert.getReservation()).getReservationId());
        existingConcert.setConcertName(concertDto.getConcertName());
        existingConcert.setDescription(concertDto.getDescription());
        existingConcert.setVenue(venueRepository.getOne(concertDto.getVenueId()));
        existingConcert.setDate(concertDto.getDate());
        existingConcert.setImage(concertDto.getImage());
        reservation.setTicketPrice(concertDto.getTicketPrice());
        concertRepository.save(existingConcert);
        return mapConcertToDto(existingConcert);
    }

    public String deleteConcert(Integer concertId) {
        Concert existingConcert = concertRepository.getOne(concertId);
        reservationRepository.deleteById(existingConcert.getReservation().getReservationId());
        concertRepository.deleteById(concertId);
        return "Concert with id:" + concertId.toString() + " was deleted.";
    }

    //Assignment1
    public ConcertDto addConcertAndReservation(ConcertDto concertDto){
        //Add Concert
        int concertId = concertRepository.save(mapConcertDto(concertDto)).getConcertId();
        //Get Concert
        Optional<Concert> optConcert = concertRepository.findById(concertId);
        Concert concert = optConcert.orElseThrow(()->new ApplicationException("Concert not found"));
        //Create Reservation
        Reservation reservation = Reservation.builder()
                .concert(concert)
                .owner(concert.getVenue().getOwner())
                .startingDate(Date.from(Instant.now()))
                .finalDate(concert.getDate())
                .ticketNumber(concert.getVenue().getCapacity())
                .ticketPrice(concertDto.getTicketPrice())
                .build();
        //Add Reservation
        reservationRepository.save(reservation);
        concertRepository.getOne(concertId).setReservation(reservation);
        concertDto.setConcertId(concertId);
        concertDto.setTicketNumber(reservation.getTicketNumber());
        concertDto.setVenue(venueService.mapVenueToVenueDto(concert.getVenue()));
        return concertDto;
    }

}
