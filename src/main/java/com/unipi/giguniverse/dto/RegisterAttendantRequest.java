package com.unipi.giguniverse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NotBlank
public class RegisterAttendantRequest {

    private String name;
    private String email;
    private String password;
}
