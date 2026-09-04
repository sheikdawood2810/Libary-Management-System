package com.example.demo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowService {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;


    // ======================================
    // BORROW BOOK
    // ======================================

    public Borrow borrowBook(Borrow borrow) {

        // Check student
        Student student = studentRepository
                .findByStudentCode(borrow.getStudentCode())
                .orElseThrow(() ->
                    new RuntimeException(
                        "Student not found"
                    )
                );


        // Check book
        Book book = bookRepository
                .findByBookCode(borrow.getBookCode())
                .orElseThrow(() ->
                    new RuntimeException(
                        "Book not found"
                    )
                );


        // Check if student already has this book
        boolean alreadyBorrowed =
                borrowRepository
                    .existsByStudentCodeAndBookCodeAndReturned(
                        borrow.getStudentCode(),
                        borrow.getBookCode(),
                        false
                    );


        if (alreadyBorrowed) {

            throw new RuntimeException(
                "Student already has this book"
            );
        }


        // Check available copies
        if (book.getAvailable() <= 0) {

            throw new RuntimeException(
                "No copies available"
            );
        }


        // Set borrowing details
        borrow.setBorrowDate(
            LocalDate.now()
        );

        borrow.setDueDate(
            LocalDate.now().plusDays(14)
        );

        borrow.setReturned(false);

        borrow.setExtensionCount(0);

        borrow.setOverdueDays(0);

        borrow.setFine(0);


        // Reduce available copies
        book.setAvailable(
            book.getAvailable() - 1
        );

        bookRepository.save(book);


        // Save borrow record
        Borrow savedBorrow =
                borrowRepository.save(borrow);


        // ==================================
        // SEND BORROW EMAIL
        // ==================================

        String subject =
            "Book Borrowed Successfully";


        String text =
            "Hello " + student.getName() + ",\n\n" +

            "You have successfully borrowed a book " +
            "from the Library Management System.\n\n" +

            "Book Title: " +
            book.getTitle() + "\n" +

            "Book Code: " +
            book.getBookCode() + "\n\n" +

            "Borrow Date: " +
            savedBorrow.getBorrowDate() + "\n" +

            "Due Date: " +
            savedBorrow.getDueDate() + "\n\n" +

            "Please return the book on or before the due date.\n\n" +

            "You can extend the book once if required.\n\n" +

            "Thank you,\n" +

            "Library Management System";


        // Email should not break borrowing
        try {

            emailService.sendEmail(
                student.getEmail(),
                subject,
                text
            );

            System.out.println(
                "Borrow confirmation email sent successfully"
            );

        } catch (Exception e) {

            System.out.println(
                "Borrow email failed: " +
                e.getMessage()
            );
        }


        return savedBorrow;
    }


    // ======================================
    // GET ALL BORROWS
    // ======================================

    public List<Borrow> getAllBorrows() {

        return borrowRepository.findAll();
    }


    // ======================================
    // STUDENT BORROW HISTORY
    // ======================================

    public List<Borrow> getStudentBorrowHistory(
            String studentCode) {

        return borrowRepository
                .findByStudentCode(studentCode);
    }


    // ======================================
    // ACTIVE BORROWS
    // ======================================

    public List<Borrow> getActiveBorrows(
            String studentCode) {

        return borrowRepository
                .findByStudentCodeAndReturned(
                    studentCode,
                    false
                );
    }


    // ======================================
    // OVERDUE BORROWS
    // ======================================

    public List<Borrow> getOverdueBorrows() {

        return borrowRepository
                .findByReturnedFalseAndDueDateBefore(
                    LocalDate.now()
                );
    }


    // ======================================
    // RETURN BOOK
    // ======================================

    public void returnBook(Long id) {

        Borrow borrow = borrowRepository
                .findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Borrow record not found"
                    )
                );


        // Check if already returned
        if (borrow.isReturned()) {

            throw new RuntimeException(
                "Book already returned"
            );
        }


        // Find student
        Student student = studentRepository
                .findByStudentCode(
                    borrow.getStudentCode()
                )
                .orElseThrow(() ->
                    new RuntimeException(
                        "Student not found"
                    )
                );


        LocalDate today =
                LocalDate.now();


        // ==================================
        // CALCULATE OVERDUE AND FINE
        // ==================================

        if (today.isAfter(borrow.getDueDate())) {

            long overdueDays =
                java.time.temporal.ChronoUnit.DAYS.between(
                    borrow.getDueDate(),
                    today
                );


            double fine =
                overdueDays * 5;


            borrow.setOverdueDays(
                overdueDays
            );

            borrow.setFine(
                fine
            );

        } else {

            borrow.setOverdueDays(0);

            borrow.setFine(0);
        }


        // Mark as returned
        borrow.setReturned(true);


        // ==================================
        // INCREASE AVAILABLE COPIES
        // ==================================

        Book book = bookRepository
                .findByBookCode(
                    borrow.getBookCode()
                )
                .orElseThrow(() ->
                    new RuntimeException(
                        "Book not found"
                    )
                );


        book.setAvailable(
            book.getAvailable() + 1
        );


        bookRepository.save(book);


        // Save returned borrow
        Borrow savedBorrow =
                borrowRepository.save(borrow);


        // ==================================
        // SEND RETURN EMAIL
        // ==================================

        String subject =
            "Book Returned Successfully";


        String text =
            "Hello " + student.getName() + ",\n\n" +

            "Your book has been successfully returned.\n\n" +

            "Book Title: " +
            book.getTitle() + "\n" +

            "Book Code: " +
            savedBorrow.getBookCode() + "\n\n" +

            "Return Date: " +
            today + "\n" +

            "Overdue Days: " +
            savedBorrow.getOverdueDays() + "\n" +

            "Fine: ₹" +
            savedBorrow.getFine() + "\n\n" +

            "Thank you,\n" +

            "Library Management System";


        // Email should not break returning
        try {

            emailService.sendEmail(
                student.getEmail(),
                subject,
                text
            );

            System.out.println(
                "Return confirmation email sent successfully"
            );

        } catch (Exception e) {

            System.out.println(
                "Return email failed: " +
                e.getMessage()
            );
        }
    }


    // ======================================
    // EXTEND BOOK
    // ======================================

    public Borrow extendBook(Long id) {

        Borrow borrow = borrowRepository
                .findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Borrow record not found"
                    )
                );


        // Cannot extend returned book
        if (borrow.isReturned()) {

            throw new RuntimeException(
                "Cannot extend a returned book"
            );
        }


        // Only one extension allowed
        if (borrow.getExtensionCount() >= 1) {

            throw new RuntimeException(
                "Book can only be extended once"
            );
        }


        // Find student
        Student student = studentRepository
                .findByStudentCode(
                    borrow.getStudentCode()
                )
                .orElseThrow(() ->
                    new RuntimeException(
                        "Student not found"
                    )
                );


        // Find book
        Book book = bookRepository
                .findByBookCode(
                    borrow.getBookCode()
                )
                .orElseThrow(() ->
                    new RuntimeException(
                        "Book not found"
                    )
                );


        // Store old due date
        LocalDate oldDueDate =
                borrow.getDueDate();


        // Add 14 days
        borrow.setDueDate(
            borrow.getDueDate().plusDays(14)
        );


        // Increase extension count
        borrow.setExtensionCount(
            borrow.getExtensionCount() + 1
        );


        // Save updated borrow
        Borrow savedBorrow =
                borrowRepository.save(borrow);


        // ==================================
        // SEND EXTENSION EMAIL
        // ==================================

        String subject =
            "Book Extension Successful";


        String text =
            "Hello " + student.getName() + ",\n\n" +

            "Your book extension was successful.\n\n" +

            "Book Title: " +
            book.getTitle() + "\n" +

            "Book Code: " +
            book.getBookCode() + "\n\n" +

            "Previous Due Date: " +
            oldDueDate + "\n" +

            "New Due Date: " +
            savedBorrow.getDueDate() + "\n\n" +

            "You have used your one allowed extension.\n\n" +

            "Please return the book on or before the new due date.\n\n" +

            "Thank you,\n" +

            "Library Management System";


        // Email should not break extension
        try {

            emailService.sendEmail(
                student.getEmail(),
                subject,
                text
            );

            System.out.println(
                "Extension confirmation email sent successfully"
            );

        } catch (Exception e) {

            System.out.println(
                "Extension email failed: " +
                e.getMessage()
            );
        }


        return savedBorrow;
    }
}