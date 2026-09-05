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

    long currentTime = System.currentTimeMillis();

    // Check if account is currently locked
    if (librarian.getLockUntil() != null &&
            currentTime < librarian.getLockUntil()) {

        long remainingMillis =
                librarian.getLockUntil() - currentTime;

        long remainingSeconds =
                remainingMillis / 1000;

        throw new RuntimeException(
                "Account locked. Try again after "
                + remainingSeconds
                + " seconds."
        );
    }

    // If lock time has expired, reset attempts
    if (librarian.getLockUntil() != null &&
            currentTime >= librarian.getLockUntil()) {

        librarian.setFailedLoginAttempts(0);
        librarian.setLockUntil(null);

        librarianRepository.save(librarian);
    }

    // Check password
    if (passwordEncoder.matches(
            password,
            librarian.getPassword())) {

        // Successful login → reset attempts
        librarian.setFailedLoginAttempts(0);
        librarian.setLockUntil(null);

        librarianRepository.save(librarian);

        return librarian;
    }

    // Wrong password
    int attempts =
            librarian.getFailedLoginAttempts() + 1;

    librarian.setFailedLoginAttempts(attempts);

    // Lock after 3 failed attempts
    if (attempts >= 3) {

        long lockTime =
                currentTime + (3 * 60 * 1000);

        librarian.setLockUntil(lockTime);

        librarianRepository.save(librarian);

        throw new RuntimeException(
                "Too many failed attempts. "
                + "Account locked for 3 minutes."
        );
    }

    librarianRepository.save(librarian);

    return null;
}
}