package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    Optional<Book> findByBookCode(String bookCode);

    List<Book> findByAuthorContainingIgnoreCase(String author);
}