package com.shadoww.authservice.contoller;

import com.shadoww.authservice.auth.request.AuthRequest;
import com.shadoww.authservice.auth.response.AuthResponse;
import com.shadoww.authservice.auth.service.AuthService;
import com.shadoww.api.dto.request.user.AuthPersonRequest;
import com.shadoww.authservice.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    @Autowired
    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody AuthPersonRequest request, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Некоректні дані для створення користувача");
        }
        Person registeredPerson = service.register(request);
        return registeredPerson != null ?
                ResponseEntity.ok("Користувач з нікнеймом " + request.getUsername() + " був успішно створений!")
                :
                ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Validated AuthRequest request,
                                                               BindingResult bindingResult){

        System.out.println(request);
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(new AuthResponse("Неправильний пароль"));
        }


        return ResponseEntity.ok(service.authenticate(request));
    }
}
