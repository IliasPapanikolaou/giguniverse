package com.unipi.giguniverse.dto;

import com.unipi.giguniverse.model.Venue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConcertDto {

    private String concertName;
    private String description;
    private Date date;
}
