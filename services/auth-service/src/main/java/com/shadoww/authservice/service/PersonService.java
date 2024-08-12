package com.shadoww.authservice.service;

import com.shadoww.authservice.model.Person;
import com.shadoww.api.service.CrudService;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface PersonService extends CrudService<Person, Long>, UserDetailsService {

    Person createSUPERADMIN();

    Person readByUsername(String username);

    Person readByEmail(String email);


    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existSUPERADMIN();


}