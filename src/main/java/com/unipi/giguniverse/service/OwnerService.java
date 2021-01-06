package com.unipi.giguniverse.service;

import com.unipi.giguniverse.dto.OwnerDto;
import com.unipi.giguniverse.exceptions.ApplicationException;
import com.unipi.giguniverse.model.Owner;
import com.unipi.giguniverse.model.User;
import com.unipi.giguniverse.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class OwnerService {
    private final UserRepository userRepository;

    private OwnerDto mapOwnerToOwnerDto(Owner owner){
        return OwnerDto.builder()
                .userId(owner.getUserId())
                .firstname(owner.getFirstname())
                .lastname(owner.getLastname())
                .email(owner.getEmail())
                .companyName(owner.getCompanyName())
                .created(owner.getCreated())
                .build();
    }

    public OwnerDto getOwner(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        Optional<User> owner =  userRepository.findByEmail(principal.getUsername());
        OwnerDto ownerDto = mapOwnerToOwnerDto((Owner) owner.orElseThrow(()->new ApplicationException("Owner not found")));
        return ownerDto;
    }

}
