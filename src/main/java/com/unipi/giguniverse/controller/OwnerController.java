package com.unipi.giguniverse.controller;

import com.unipi.giguniverse.dto.OwnerDto;
import com.unipi.giguniverse.dto.VenueDto;
import com.unipi.giguniverse.service.OwnerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("api/owner")
@AllArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @GetMapping
    public ResponseEntity<OwnerDto> getOwner(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ownerService.getOwner());
    }
}
