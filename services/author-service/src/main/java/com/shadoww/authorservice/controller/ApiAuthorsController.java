package com.shadoww.authorservice.controller;

import com.shadoww.api.dto.request.AuthorRequest;
import com.shadoww.api.dto.response.AuthorResponse;
import com.shadoww.api.exception.ValueAlreadyExistsException;
import com.shadoww.authorservice.mapper.AuthorMapper;
import com.shadoww.authorservice.model.Author;
import com.shadoww.authorservice.service.interfaces.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/authors")
public class ApiAuthorsController {

    private final AuthorService authorService;

    private final AuthorMapper authorMapper;
    @Autowired
    public ApiAuthorsController(
            AuthorService authorService, AuthorMapper authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @GetMapping
    public ResponseEntity<?> getAuthors() {
        return ResponseEntity.ok(authorService.getAll()
                .stream()
                .map(authorMapper::dtoToResponse)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthor(@PathVariable long id) {
        return ResponseEntity.ok(
                authorMapper.dtoToResponse(authorService.readById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthor(@PathVariable long id,
                                          @RequestBody AuthorRequest request) {

        System.out.println(request);

        if (authorService.existsByName(request.getName())) {
            throw new ValueAlreadyExistsException("Автор з таким іменем вже існує!");
        }

        Author author = authorService.readById(id);

        if(request.getName() != null) {
            author.setName(request.getName());
        }
        if(request.getBiography() != null) {
            author.setBiography(request.getBiography());
        }

        return ResponseEntity.ok(authorMapper.dtoToResponse(authorService.update(author)));
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable long id) {
        authorService.deleteById(id);

        return ResponseEntity.ok("Автор був видалений");
    }
}