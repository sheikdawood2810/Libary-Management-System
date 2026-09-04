package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;




    @PostMapping
    public Student saveStudent(
            @RequestBody Student student) {

        return studentService.saveStudent(student);
    }



    @GetMapping
    public List<Student> getAllStudents() {

        return studentService.getAllStudents();
    }


    // ==============================
    // GET STUDENT BY CODE
    // ==============================

    @GetMapping("/{studentCode}")
    public ResponseEntity<?> getStudentByCode(
            @PathVariable String studentCode) {

        Student student =
                studentService.getStudentByCode(studentCode);

        if (student == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Student not found");
        }

        return ResponseEntity.ok(
            new LoginResponse(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getStudentCode()
            )
        );
    }


    @DeleteMapping("/{id}")
    public void deleteStudent(
            @PathVariable Long id) {

        studentService.deleteStudent(id);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Student student) {

        Student loggedInStudent =
                studentService.login(
                    student.getStudentCode(),
                    student.getPassword()
                );

        if (loggedInStudent == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid student ID or password");
        }

        return ResponseEntity.ok(
            new LoginResponse(
                loggedInStudent.getId(),
                loggedInStudent.getName(),
                loggedInStudent.getEmail(),
                loggedInStudent.getStudentCode()
            )
        );
    }
}