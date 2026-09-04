package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;


    @Transactional
public Student saveStudent(Student student) {

    // Validate email format
    if (student.getEmail() == null ||
        !student.getEmail().matches(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )) {

        throw new RuntimeException(
            "Please enter a valid email address"
        );
    }

    // Hash password before saving

        // Hash password before saving
        student.setPassword(
            passwordEncoder.encode(
                student.getPassword()
            )
        );


        // Save first so database generates ID
        Student savedStudent =
            studentRepository.save(student);


        // Generate Student Code
        savedStudent.setStudentCode(
            String.format(
                "STU-%06d",
                savedStudent.getId()
            )
        );


        // Save again with Student Code
        savedStudent =
            studentRepository.save(savedStudent);


        String subject =
            "Welcome to Library Management System";


        String text =
            "Hello " + savedStudent.getName() + ",\n\n" +

            "Your registration was successful.\n\n" +

            "Student ID: " +
            savedStudent.getStudentCode() +
            "\n\n" +

            "You can now use your Student ID " +
            "and password to log in.\n\n" +

            "Thank you,\n" +
            "Library Management System";


        try {

    emailService.sendEmail(
        savedStudent.getEmail(),
        subject,
        text
    );

    System.out.println(
        "Welcome email sent successfully"
    );

} catch (Exception e) {

    System.out.println(
        "Welcome email failed: " +
        e.getMessage()
    );
}


        return savedStudent;
    }



    public Student login(
            String studentCode,
            String password) {

        Student student =
            studentRepository
                .findByStudentCode(studentCode)
                .orElse(null);


        if (student == null) {
            return null;
        }


        if (
            passwordEncoder.matches(
                password,
                student.getPassword()
            )
        ) {

            return student;
        }


        return null;
    }



    public Student getStudentByCode(
            String studentCode) {

        return studentRepository
            .findByStudentCode(studentCode)
            .orElse(null);
    }



    public List<Student> getAllStudents() {

        return studentRepository.findAll();
    }



    public void deleteStudent(Long id) {

        studentRepository.deleteById(id);
    }
}