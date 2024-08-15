package com.shadoww.authservice.mapper.impl;

import com.shadoww.api.dto.request.user.PersonRequest;
import com.shadoww.api.dto.response.PersonResponse;
import com.shadoww.authservice.mapper.PersonMapper;
import com.shadoww.authservice.model.Person;
import com.shadoww.authservice.model.Role;
import com.shadoww.authservice.model.Theme;
import org.springframework.stereotype.Component;

@Component
public class PersonMapperImpl implements PersonMapper {
    @Override
    public Person mapToModel(PersonRequest request) {
        Person person = new Person();

        person.setEmail(request.getEmail());
        person.setUsername(request.getUsername());
        person.setPassword(request.getPassword());
        person.setRole(Role.valueOf(request.getRole().toUpperCase()));

        return person;
    }

    @Override
    public PersonResponse mapToResponse(Person person) {
        return new PersonResponse(
                person.getId(),
                person.getEmail(),
                person.getUsername(),
                person.getRole().name(),
                person.getTheme().name(),
                person.getPersonImageId() != null ? person.getPersonImageId().toString() : null);
    }

    @Override
    public Person mapToModel(PersonResponse response) {
        Person person = new Person();

        person.setId(response.getId());
        person.setUsername(response.getUsername());
        person.setPassword(null);
        person.setRole(Role.valueOf(response.getRoleName().toUpperCase()));
        person.setTheme(Theme.valueOf(response.getTheme().toUpperCase()));

        return person;
    }
}