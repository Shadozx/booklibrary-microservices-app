package com.shadoww.authservice.repository;


import com.shadoww.authservice.model.Person;
import com.shadoww.authservice.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    boolean existsByUsernameIgnoreCase(String username);

    Page<Person> findByUsernameContainingIgnoreCase(Pageable page, String username);

    Optional<Person> findByUsername(String username);
    Optional<Person> findByEmail(String email);

    boolean existsByRole(Role role);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
