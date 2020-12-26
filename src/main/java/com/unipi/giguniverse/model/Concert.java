package com.unipi.giguniverse.model;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Concert {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int concertId;
    private String concertName;
    @ManyToOne(fetch = LAZY)
    private Venue venue;
    @Nullable
    @Lob
    private String description;
    private Date date;
    @Nullable
    @OneToOne(fetch = LAZY)
    private Reservation reservation;

}
