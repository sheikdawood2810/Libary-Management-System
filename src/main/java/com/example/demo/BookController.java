package com.example.demo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private Bookservice bookservice;

    @PostMapping
    public Book saveBook(@RequestBody Book book) {
        return bookservice.saveBook(book);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookservice.getAllBooks();
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookservice.updateBook(id, book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookservice.deleteBook(id);
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String title) {
    return bookservice.searchBooks(title);
}
   @GetMapping("/search/author")
   public List<Book> searchbyAuthor(@RequestParam String author){
    return bookservice.searchByAuthor(author);
   }
   
}