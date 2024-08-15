package com.shadoww.authservice.auth.service;

import com.shadoww.jwtsecurity.auth.JwtService;
import com.shadoww.api.dto.request.user.AuthPersonRequest;
import com.shadoww.authservice.auth.request.AuthRequest;
import com.shadoww.authservice.auth.response.AuthResponse;
import com.shadoww.authservice.model.Person;
import com.shadoww.authservice.security.AuthUser;
import com.shadoww.authservice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PersonService personService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(PersonService personService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.personService = personService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public Person register(AuthPersonRequest request) {

        Person newUser = new Person();
        newUser.setEmail(request.getEmail());
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());
        return personService.create(newUser);
    }

    public AuthResponse authenticate(AuthRequest request) {
        Person person = personService.readByEmail(request.getEmail());


        System.out.println(person);
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        authenticationManager.authenticate(authenticationToken);

        return new AuthResponse(jwtService.generateToken(new AuthUser(person)));
    }

}