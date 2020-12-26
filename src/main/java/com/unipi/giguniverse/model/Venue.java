package com.unipi.giguniverse.model;

import lombok.*;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Venue {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int venueId;
    @NonNull
    private String venueName;
    @OneToOne(fetch = LAZY)
    private Owner owner;
    @NonNull
    private String address;
    @NonNull
    private String city;
    @NonNull
    private String phone;
    private int capacity;
    @OneToMany(fetch = LAZY)
    private List<Concert> concert;
}
