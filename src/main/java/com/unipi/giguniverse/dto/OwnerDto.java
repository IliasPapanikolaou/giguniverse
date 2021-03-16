package com.unipi.giguniverse.dto;

import com.unipi.giguniverse.model.Reservation;
import com.unipi.giguniverse.model.User;
import com.unipi.giguniverse.model.Venue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OwnerDto {

    private Integer userId;
    private String firstname;
    private String lastname;
    private String email;
    private String companyName;
    private Instant created;
    private MultipartFile image;

}
