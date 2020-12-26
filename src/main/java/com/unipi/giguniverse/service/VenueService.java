package com.unipi.giguniverse.service;

import com.unipi.giguniverse.dto.VenueDto;
import com.unipi.giguniverse.exceptions.ApplicationException;
import com.unipi.giguniverse.model.Owner;
import com.unipi.giguniverse.model.Venue;
import com.unipi.giguniverse.repository.VenueRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class VenueService {

    private final VenueRepository venueRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;


    public VenueDto addVenue(VenueDto venueDto){
        //Check by VenueName if venue already exists in DB
        if(!venueRepository.existsVenueByVenueName(venueDto.getVenueName())){
            //Add venue to DB
            venueRepository.save(mapVenueDto(venueDto));
            return venueDto;
        }
        else {
            throw new ApplicationException("Venue already exists");
        }
    }

    public List<VenueDto> getAllVenues(){
        List<VenueDto> venues = venueRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(toList());
        return venues;
    }

    private VenueDto mapToDto(Venue venue){
        return VenueDto.builder()
                .venueName(venue.getVenueName())
                .address(venue.getAddress())
                .city(venue.getCity())
                .phone(venue.getPhone())
                .capacity(venue.getCapacity())
                .build();
    }

    private Venue mapVenueDto(VenueDto venueDto) {
        return Venue.builder()
                .venueName(venueDto.getVenueName())
                .owner((Owner) userDetailsServiceImpl.getCurrentUserDetails())
                .address(venueDto.getAddress())
                .city(venueDto.getCity())
                .phone(venueDto.getPhone())
                .capacity(venueDto.getCapacity())
                .build();
    }

    public VenueDto getVenueById(Integer id){
        Optional<Venue> venue =  venueRepository.findById(id);
        VenueDto venueDto = mapToDto(venue.orElseThrow(()->new ApplicationException("Venue not found")));
        return venueDto;
    }

    public List<VenueDto> getVenueByCity(String city){
        List<VenueDto> venues = venueRepository.findAllByCity(city)
                .stream()
                .map(this::mapToDto)
                .collect(toList());
        return venues;
    }

}
