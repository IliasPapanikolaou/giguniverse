package com.unipi.giguniverse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendantDto {

    private Integer userId;
    private String firstname;
    private String lastname;
    private String email;
    private Instant created;
    private MultipartFile image;

}
