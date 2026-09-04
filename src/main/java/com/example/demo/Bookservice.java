package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Bookservice {

    @Autowired
    private BookRepository bookRepository;



    public Book saveBook(Book book) {

        // Prevent negative available copies
        if (book.getAvailable() < 0) {

            throw new IllegalArgumentException(
                "Available copies cannot be negative"
            );
        }


        Book savedBook =
                bookRepository.save(book);


        savedBook.setBookCode(
            String.format(
                "BK-%06d",
                savedBook.getId()
            )
        );


        return bookRepository.save(savedBook);
    }


    public List<Book> getAllBooks() {

        return bookRepository.findAll();
    }


    public Book updateBook(
            Long id,
            Book book) {

        // Prevent negative available copies
        if (book.getAvailable() < 0) {

            throw new IllegalArgumentException(
                "Available copies cannot be negative"
            );
        }


        Book existingBook =
                bookRepository
                    .findById(id)
                    .orElseThrow();


        existingBook.setTitle(
            book.getTitle()
        );

        existingBook.setAuthor(
            book.getAuthor()
        );

        existingBook.setCategory(
            book.getCategory()
        );

        existingBook.setAvailable(
            book.getAvailable()
        );


        return bookRepository.save(
            existingBook
        );
    }


    public void deleteBook(Long id) {

        bookRepository.deleteById(id);
    }



    public List<Book> searchBooks(
            String title) {

        return bookRepository
            .findByTitleContainingIgnoreCase(title);
    }


    public List<Book> searchByAuthor(
            String author) {

        return bookRepository
            .findByAuthorContainingIgnoreCase(author);
    }
}