package com.unipi.giguniverse.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Inheritance //Default value Single Table that includes ancestors
@Data //Lombok, Generates Setters - Getters
@SuperBuilder //Builder that supports super
@NoArgsConstructor
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

