package com.unipi.giguniverse.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Attendant extends User{

    @OneToMany
    private List<Ticket> tickets;
}
