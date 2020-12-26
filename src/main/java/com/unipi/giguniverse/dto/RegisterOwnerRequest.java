package com.unipi.giguniverse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterOwnerRequest {

    private String name;
    private String email;
    private String password;
    private String companyName;

}
