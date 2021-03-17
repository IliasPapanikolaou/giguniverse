package com.unipi.giguniverse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConcertDto {

    private Integer concertId;
    private String concertName;
    private String description;
    private Integer venueId;
    private VenueDto venue;
    private Date date;
    private String image;
}
