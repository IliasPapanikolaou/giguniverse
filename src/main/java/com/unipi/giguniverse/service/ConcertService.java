package com.unipi.giguniverse.service;

import com.unipi.giguniverse.dto.ConcertDto;
import com.unipi.giguniverse.exceptions.ApplicationException;
import com.unipi.giguniverse.model.Concert;
import com.unipi.giguniverse.model.Venue;
import com.unipi.giguniverse.repository.ConcertRepository;



import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ConcertService {

    private final ConcertRepository concertRepository;

    private ConcertDto mapConcertToDto(Concert concert){
        return ConcertDto.builder()
                .concertName(concert.getConcertName())
                .description(concert.getDescription())
                .date(concert.getDate())
                .build();
    }

    private Concert mapConcertDto(ConcertDto concertDto){
        return Concert.builder()
                .concertName(concertDto.getConcertName())
                .description(concertDto.getDescription())
                .date(concertDto.getDate())
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

    public List<ConcertDto> getConcertByDate(Date date){
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

    public List<ConcertDto> getConcertByMonth(Integer month){
        //TODO use sql "between" or create attribute month in Concert class
        return Collections.emptyList();
    }


}