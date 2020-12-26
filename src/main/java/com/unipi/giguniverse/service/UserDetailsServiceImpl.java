package com.unipi.giguniverse.service;

import com.unipi.giguniverse.exceptions.ApplicationException;
import com.unipi.giguniverse.model.User;
import com.unipi.giguniverse.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

import static java.util.Collections.singletonList;

//Implementation of UserDetailsService Interface
@Service
@AllArgsConstructor
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow(()->new ApplicationException("User not found"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), user.getIsEnabled(),
                true, true , true,
                getAuthorities("USER"));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role){
        return singletonList(new SimpleGrantedAuthority(role));
    }

    //Get current logged in user details from DB
    public User getCurrentUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        Optional<User> userOptional = userRepository.findByEmail(principal.getUsername());
        User user = userOptional.orElseThrow(()->new ApplicationException("User not found"));
        return user;
    }
}
