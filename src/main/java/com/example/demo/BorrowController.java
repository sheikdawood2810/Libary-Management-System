package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/borrows")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;


    // ======================================
    // BORROW BOOK
    // ======================================

    @PostMapping
    public ResponseEntity<?> borrowBook(
            @RequestBody Borrow borrow) {

        try {

            return ResponseEntity.ok(
                borrowService.borrowBook(borrow)
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // ======================================
    // GET ALL BORROWS
    // ======================================

    @GetMapping
    public List<Borrow> getAllBorrows() {

        return borrowService.getAllBorrows();
    }


    // ======================================
    // RETURN BOOK
    // ======================================

    @PutMapping("/return/{id}")
    public ResponseEntity<?> returnBook(
            @PathVariable Long id) {

        try {

            borrowService.returnBook(id);

            return ResponseEntity.ok(
                "Book returned successfully"
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // ======================================
    // EXTEND BOOK
    // ======================================

    @PutMapping("/extend/{id}")
    public ResponseEntity<?> extendBook(
            @PathVariable Long id) {

        try {

            return ResponseEntity.ok(
                borrowService.extendBook(id)
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // ======================================
    // STUDENT BORROW HISTORY
    // ======================================

    @GetMapping("/student/{studentCode}")
    public List<Borrow> getStudentBorrowHistory(
            @PathVariable String studentCode) {

        return borrowService
                .getStudentBorrowHistory(studentCode);
    }


    // ======================================
    // ACTIVE BORROWS
    // ======================================

    @GetMapping("/student/{studentCode}/active")
    public List<Borrow> getActiveBorrows(
            @PathVariable String studentCode) {

        return borrowService
                .getActiveBorrows(studentCode);
    }


    // ======================================
    // OVERDUE BORROWS
    // ======================================

    @GetMapping("/overdue")
    public List<Borrow> getOverdueBorrows() {

        return borrowService.getOverdueBorrows();
    }
}