package com.unipi.giguniverse.service;

import com.unipi.giguniverse.dto.AuthenticationResponse;
import com.unipi.giguniverse.dto.LoginRequest;
import com.unipi.giguniverse.dto.RegisterOwnerRequest;
import com.unipi.giguniverse.exceptions.ApplicationException;
import com.unipi.giguniverse.model.NotificationEmail;
import com.unipi.giguniverse.model.Owner;
import com.unipi.giguniverse.model.User;
import com.unipi.giguniverse.model.VerificationToken;
import com.unipi.giguniverse.repository.UserRepository;
import com.unipi.giguniverse.repository.VerificationTokenRepository;
import com.unipi.giguniverse.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;
    private final JwtProvider jwtProvider;

    public void ownerSignup(RegisterOwnerRequest registerOwnerRequest){
        User owner = new Owner();
        owner.setName(registerOwnerRequest.getName());
        owner.setEmail(registerOwnerRequest.getEmail());
        owner.setPassword(passwordEncoder.encode(registerOwnerRequest.getPassword()));
        owner.setCreated(Instant.now());
        owner.setIsEnabled(false);

        //Check DB for duplicate account by email
        if(!userRepository.existsUserByEmail(registerOwnerRequest.getEmail())){

            userRepository.save(owner); // Save User to DB

            String token = generateVerificationToken(owner);
            mailService.sendMail(new NotificationEmail("Please Activate your Account",
                    owner.getEmail(), "Please click on the link below to activate" +
                    "your account: http://localhost:8080/api/auth/accountVerification/" +token));
        }
        else {
            throw new ApplicationException("User already exists");
        }
    }

    //Create Verification token
    private String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    //Account Verification
    public void verifyAccount(String token){
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(()->new ApplicationException("Invalid Token"));
        //Fetch and Enable User
        fetchUserAndEnable(verificationToken.get());
        //Delete Verification Token from DB
        deleteVerificationToken(token);
    }

    //Check if token is valid
    private void fetchUserAndEnable(VerificationToken verificationToken){
        Integer userId = verificationToken.getUser().getUserId();
        User user = userRepository.findById(userId).orElseThrow(()->new ApplicationException("User not found"));
        user.setIsEnabled(true);
        userRepository.save(user);
    }

    //Delete Verification Token
    private void deleteVerificationToken(String token){
        verificationTokenRepository.deleteByToken(token);
    }

    public AuthenticationResponse login(LoginRequest loginRequest){
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(token, loginRequest.getEmail());
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
