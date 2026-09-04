package com.example.demo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    List<Borrow> findByStudentCode(String studentCode);

    List<Borrow> findByStudentCodeAndReturned(
        String studentCode,
        boolean returned
    );

    List<Borrow> findByReturnedFalseAndDueDateBefore(
        LocalDate date
    );

    boolean existsByStudentCodeAndBookCodeAndReturned(
        String studentCode,
        String bookCode,
        boolean returned
    );
}