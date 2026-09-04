package com.example.demo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ReminderService {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EmailService emailService;


    // ======================================
    // CHECK DUE DATES
    // Runs every 24 hours
    // ======================================

    @Scheduled(fixedRate = 86400000)
    public void checkDueDates() {

        System.out.println("REMINDER SERVICE CHECKING...");

        List<Borrow> borrows = borrowRepository.findAll();

        LocalDate today = LocalDate.now();

        for (Borrow borrow : borrows) {

            // Ignore returned books
            if (borrow.isReturned()) {
                continue;
            }

            LocalDate dueDate = borrow.getDueDate();

            long daysLeft = ChronoUnit.DAYS.between(
                today,
                dueDate
            );


            // ==================================
            // 1. DUE DATE REMINDER
            // 3, 2 OR 1 DAYS BEFORE DUE DATE
            // ==================================

            if (daysLeft >= 1 && daysLeft <= 3) {

                System.out.println("--------------------------------");
                System.out.println("BOOK DUE REMINDER");
                System.out.println("Student: " + borrow.getStudentCode());
                System.out.println("Book: " + borrow.getBookCode());
                System.out.println("Due Date: " + dueDate);
                System.out.println("Days Remaining: " + daysLeft);
                System.out.println("--------------------------------");

                sendReminderEmail(
                    borrow,
                    daysLeft
                );
            }


            // ==================================
            // 2. OVERDUE NOTIFICATION
            // ==================================

            if (daysLeft < 0 &&
                !borrow.isOverdueNotificationSent()) {

                System.out.println("--------------------------------");
                System.out.println("BOOK IS OVERDUE");
                System.out.println("Student: " + borrow.getStudentCode());
                System.out.println("Book: " + borrow.getBookCode());
                System.out.println("Due Date: " + dueDate);
                System.out.println("Overdue Days: " + Math.abs(daysLeft));
                System.out.println("--------------------------------");

                sendOverdueEmail(
                    borrow,
                    Math.abs(daysLeft)
                );
            }
        }
    }


    // ======================================
    // SEND DUE DATE REMINDER EMAIL
    // ======================================

    private void sendReminderEmail(
            Borrow borrow,
            long daysLeft) {

        Student student =
                studentRepository
                    .findByStudentCode(
                        borrow.getStudentCode()
                    )
                    .orElse(null);

        Book book =
                bookRepository
                    .findByBookCode(
                        borrow.getBookCode()
                    )
                    .orElse(null);

        if (student == null || book == null) {

            System.out.println(
                "Unable to send due-date reminder."
            );

            return;
        }


        String subject;

        if (daysLeft == 3) {

            subject = "Book Due in 3 Days";

        } else if (daysLeft == 2) {

            subject = "Book Due in 2 Days";

        } else {

            subject = "Book Due Tomorrow";
        }


        String text =
                "Hello " +
                student.getName() +
                ",\n\n" +

                "This is a reminder about your borrowed book.\n\n" +

                "Book Title: " +
                book.getTitle() +
                "\n" +

                "Book Code: " +
                book.getBookCode() +
                "\n\n" +

                "Due Date: " +
                borrow.getDueDate() +
                "\n" +

                "Days Remaining: " +
                daysLeft +
                "\n\n" +

                "Please return the book on or before the due date " +
                "to avoid overdue fines.\n\n" +

                "Thank you,\n" +
                "Library Management System";


        try {

            emailService.sendEmail(
                student.getEmail(),
                subject,
                text
            );

            System.out.println(
                "Due-date reminder email sent to: " +
                student.getEmail()
            );

        } catch (Exception e) {

            System.out.println(
                "Due-date reminder email failed: " +
                e.getMessage()
            );
        }
    }


    // ======================================
    // SEND OVERDUE EMAIL
    // ======================================

    private void sendOverdueEmail(
            Borrow borrow,
            long overdueDays) {

        Student student =
                studentRepository
                    .findByStudentCode(
                        borrow.getStudentCode()
                    )
                    .orElse(null);

        Book book =
                bookRepository
                    .findByBookCode(
                        borrow.getBookCode()
                    )
                    .orElse(null);


        if (student == null || book == null) {

            System.out.println(
                "Unable to send overdue notification."
            );

            return;
        }


        // ==================================
        // CALCULATE FINE
        // ₹5 PER OVERDUE DAY
        // ==================================

        double fine = overdueDays * 5;

        borrow.setOverdueDays(overdueDays);
        borrow.setFine(fine);


        // ==================================
        // EMAIL SUBJECT
        // ==================================

        String subject =
                "Book Overdue - Please Return";


        // ==================================
        // EMAIL MESSAGE
        // ==================================

        String text =
                "Hello " +
                student.getName() +
                ",\n\n" +

                "Your borrowed book is now overdue.\n\n" +

                "Book Title: " +
                book.getTitle() +
                "\n" +

                "Book Code: " +
                book.getBookCode() +
                "\n\n" +

                "Due Date: " +
                borrow.getDueDate() +
                "\n" +

                "Overdue Days: " +
                overdueDays +
                "\n" +

                "Current Fine: ₹" +
                fine +
                "\n\n" +

                "Please return the book as soon as possible " +
                "to avoid additional overdue fines.\n\n" +

                "Thank you,\n" +
                "Library Management System";


        // ==================================
        // SEND EMAIL
        // ==================================

        try {

            emailService.sendEmail(
                student.getEmail(),
                subject,
                text
            );


            // Mark notification as sent
            borrow.setOverdueNotificationSent(true);

            borrowRepository.save(borrow);


            System.out.println(
                "Overdue notification email sent to: " +
                student.getEmail()
            );

        } catch (Exception e) {

            System.out.println(
                "Overdue notification email failed: " +
                e.getMessage()
            );
        }
    }
}