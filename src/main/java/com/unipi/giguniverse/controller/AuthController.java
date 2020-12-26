package com.unipi.giguniverse.controller;

import com.unipi.giguniverse.dto.AuthenticationResponse;
import com.unipi.giguniverse.dto.LoginRequest;
import com.unipi.giguniverse.dto.RegisterOwnerRequest;
import com.unipi.giguniverse.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> ownerSignup(@RequestBody RegisterOwnerRequest registerOwnerRequest){
        authService.ownerSignup(registerOwnerRequest);
        return new ResponseEntity<>("Owner Registration Successful", HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }


}
