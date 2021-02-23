package com.unipi.giguniverse.repository;

import com.unipi.giguniverse.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Integer> {

    List<Reservation> findByOwnerUserId(int userId);
}
