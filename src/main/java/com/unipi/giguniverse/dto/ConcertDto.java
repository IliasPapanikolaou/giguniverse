package com.unipi.giguniverse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

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
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate date;
}
