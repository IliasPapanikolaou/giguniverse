package com.unipi.giguniverse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Owner extends User{

    private String companyName;
    @OneToMany(fetch = LAZY)
    private List<Venue> venue;
    @OneToMany(fetch = LAZY)
    private List<Reservation> reservation;
}
