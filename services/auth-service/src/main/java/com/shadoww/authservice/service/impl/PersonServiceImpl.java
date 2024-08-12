package com.shadoww.authservice.service.impl;

import com.shadoww.api.exception.NotFoundException;
import com.shadoww.api.exception.NullEntityReferenceException;
import com.shadoww.api.exception.ValueAlreadyExistsException;
import com.shadoww.authservice.model.Person;
import com.shadoww.authservice.model.Role;
import com.shadoww.authservice.model.Theme;
import com.shadoww.authservice.repository.PersonRepository;
import com.shadoww.authservice.security.AuthUser;
import com.shadoww.authservice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Person create(Person person) {

        checkIsPersonNull(person);

        if (existsByEmail(person.getEmail())) {
            throw new ValueAlreadyExistsException("Email вже використовується");
        }

        if (existsByUsername(person.getUsername())) {
            throw new ValueAlreadyExistsException("Нікнейм вже використовується");
        }

        person.setPassword(passwordEncoder.encode(person.getPassword()));

        return save(person);
    }

    @Override
    @Transactional
    public Person createSUPERADMIN() {
        Person person = new Person();

        person.setUsername("super_admin");
        person.setPassword(passwordEncoder.encode("super_admin"));
        person.setEmail("superadmin@gmail.com");
        person.setRole(Role.SUPER_ADMIN);
        person.setTheme(Theme.DARK);

        checkIsPersonNull(person);

        return save(person);
    }

    @Override
    public Person readByUsername(String username) {
        return personRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new NotFoundException(
                                String.format("Користувача з нікнеймом %s не існує", username)));
    }

    @Override
    public Person readByEmail(String email) {

        return personRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new NotFoundException(
                                String.format("Користувача з таким email %s не існує", email)));
    }

    @Override
    public Person readById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Такого користувача не існує"));
    }

    @Override
    public boolean existsById(Long id) {
        return personRepository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return personRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return personRepository.existsByUsername(username);
    }

    @Override
    public boolean existSUPERADMIN() {
        return personRepository.existsByRole(Role.SUPER_ADMIN);
    }


    @Override
    @Transactional
    public Person update(Person person) {

        checkIsPersonNull(person);

        readById(person.getId());

        return save(person);
    }

    @Transactional
    public void deleteById(Long id) {
        delete(readById(id));
    }

    @Override
    public long count() {
        return personRepository.count();
    }

    @Override
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return new AuthUser(readByEmail(username));
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    @Transactional
    Person save(Person person) {
        return personRepository.save(person);
    }

    @Transactional
    void delete(Person person) {
        personRepository.delete(person);
    }

    private void checkIsPersonNull(Person person) {
        if (person == null) {
            throw new NullEntityReferenceException("Користувач не може бути пустим");
        }
    }
}
