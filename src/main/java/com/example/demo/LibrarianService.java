package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LibrarianService {

    @Autowired
    private LibrarianRepository librarianRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Librarian saveLibrarian(Librarian librarian) {

        librarian.setPassword(
            passwordEncoder.encode(librarian.getPassword())
        );

        return librarianRepository.save(librarian);
    }


    public Librarian login(String username, String password) {

        Librarian librarian = librarianRepository
                .findByUsername(username)
                .orElse(null);

        if (librarian == null) {
            return null;
        }

        if (passwordEncoder.matches(
                password,
                librarian.getPassword())) {

            return librarian;
        }

        return null;
    }
}