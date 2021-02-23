package com.unipi.giguniverse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    private Integer reservationId;
    private Integer concertId;
    private ConcertDto concert;
    private LocalDate startingDate;
    private LocalDate finalDate;
    private Integer ticketNumber;
}
