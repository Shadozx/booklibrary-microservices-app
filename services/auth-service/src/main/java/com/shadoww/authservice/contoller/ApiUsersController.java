package com.shadoww.authservice.contoller;


import com.shadoww.api.dto.request.user.PersonRequest;
import com.shadoww.authservice.mapper.PersonMapper;
import com.shadoww.authservice.model.Person;
import com.shadoww.authservice.model.Role;
import com.shadoww.authservice.model.Theme;
import com.shadoww.authservice.security.AuthUser;
import com.shadoww.authservice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


//@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/users")
public class ApiUsersController {

    private final PersonService personService;

    private final PasswordEncoder passwordEncoder;

    private final PersonMapper personMapper;

    @Autowired
    public ApiUsersController(PersonService personService,
                              PasswordEncoder passwordEncoder, PersonMapper personMapper) {
        this.personService = personService;
        this.passwordEncoder = passwordEncoder;
        this.personMapper = personMapper;
    }

    @GetMapping
    public ResponseEntity<?> getUsers() {
        return sendOk(personService.getAll()
                .stream()
                .map(personMapper::mapToResponse)
                .toList());
    }

/*
    @PreAuthorize("hasRole('SUPER_ADMIN')")
*/
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody PersonRequest form) {

        if (form.isEmpty()) return sendNoContent("Форма для реєстрації пуста");

        if (form.isUsernameEmpty()) return sendNoContent("Імя не може бути пустим");
        if (form.isPasswordEmpty()) return sendNoContent("Пароль не може бути пустим");

//        if (personService.existByUsername(form.getUsername())) return ResponseUser.exist();

        Person person = personMapper.mapToModel(form);

        return sendCreated(personService.create(person));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable long id) {
        Person person = personService.readById(id);

        return sendOk(personMapper.mapToResponse(person));
    }

//    @PreAuthorize("hasRole('SUPER_ADMIN') || #userId == principal.id")
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserById(@PathVariable long userId,
                                            @RequestBody PersonRequest form) {

        System.out.println("Form: " + form);

        Person person = personService.readById(userId);


//        if (form.isEmpty()) return ResponseUser.noContent();

        if (!form.isUsernameEmpty()) person.setUsername(form.getUsername());

        if (!form.isPasswordEmpty()) person.setPassword(passwordEncoder.encode(form.getPassword()));

        if (!form.isRoleEmpty()) person.setRole(Role.valueOf(form.getRole().toUpperCase()));

        System.out.println("Updated person: " + person);


//        return ResponseUser.updateSuccess();

        return sendOk(personMapper.mapToResponse(personService.create(person)));

    }

//    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deletePerson(@PathVariable long userId) {

        personService.deleteById(userId);

        return sendOk();
    }

//    @PreAuthorize("hasRole('SUPER_ADMIN') || #userId == principal.id")
    @PostMapping("/{userId}/theme")
    public ResponseEntity<?> changeTheme(@PathVariable long userId) {
        Person person = personService.readById(userId);

        Theme theme = person.getTheme();

        person.setTheme(Theme.next(theme));

        personService.update(person);

        return sendOk("Тема профіля користувача була успішно оновлена");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        AuthUser authUser = (AuthUser) authentication.getPrincipal();

        Person person = authUser.getPerson();

        return sendResponse(HttpStatus.OK, personMapper.mapToResponse(person));
    }

    private <T> ResponseEntity<T> sendResponse(HttpStatus status, T body) {

        return ResponseEntity.status(status).body(body);
    }
    private ResponseEntity<?> sendResponse(HttpStatus status) {

        return ResponseEntity.status(status).build();
    }

    private ResponseEntity<?> sendOk() {
        return sendResponse(HttpStatus.OK);
    }

    private <T> ResponseEntity<T> sendOk(T body) {
        return sendResponse(HttpStatus.OK, body);
    }

    private ResponseEntity<?> sendNoContent() {
        return sendResponse(HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<?> sendCreated() {
        return sendResponse(HttpStatus.CREATED);
    }
    private <T> ResponseEntity<T> sendCreated(T body) {
        return sendResponse(HttpStatus.CREATED, body);
    }

    private <T> ResponseEntity<T> sendNoContent(T body) {
        return sendResponse(HttpStatus.NO_CONTENT, body);
    }
}