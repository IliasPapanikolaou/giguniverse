package com.unipi.giguniverse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAttendantRequest {
    @NotBlank(message="Name is required")
    private String name;
    @NotBlank(message="Email is required")
    private String email;
    @Email
    @NotBlank(message="Password is required")
    private String password;
}
