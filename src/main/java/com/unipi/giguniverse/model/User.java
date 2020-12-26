package com.unipi.giguniverse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Inheritance //Default value Single Table that includes ancestors
@AllArgsConstructor //Lombok, Generates Constructor
@NoArgsConstructor
@Data //Lombok, Generates Setters - Getters
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    protected int userId;
    @NonNull
    protected String name;
    @NonNull
    protected String email;
    @NonNull
    protected String password;
    protected Instant created;
    protected Boolean isEnabled = false;
}

