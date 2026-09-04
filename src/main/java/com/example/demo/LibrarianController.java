package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/librarian")
public class LibrarianController {

    @Autowired
    private LibrarianService librarianService;


    @PostMapping
    public Librarian saveLibrarian(
            @RequestBody Librarian librarian) {

        return librarianService.saveLibrarian(librarian);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Librarian librarian) {

        Librarian loggedInLibrarian =
                librarianService.login(
                    librarian.getUsername(),
                    librarian.getPassword()
                );


        if (loggedInLibrarian == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }


        return ResponseEntity.ok(
            new LibrarianLoginResponse(
                loggedInLibrarian.getId(),
                loggedInLibrarian.getUsername()
            )
        );
    }
}
