package com.tpe.student_management.controller;

import com.tpe.student_management.dto.request.StudentCreateRequestDTO;
import com.tpe.student_management.dto.request.StudentSelectLessonDTO;
import com.tpe.student_management.dto.request.StudentSelfUpdateRequestDTO;
import com.tpe.student_management.dto.response.UserResponseDTO;
import com.tpe.student_management.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserResponseDTO> saveTeacher(@RequestBody @Valid StudentCreateRequestDTO dto) {
        return new ResponseEntity<>(studentService.save(dto), HttpStatus.CREATED);
    }

    //* 1 - updateStudentOwnInfo() - optional
    @PatchMapping("/update")   // http://localhost:8080/student/update
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseEntity<String> updateStudentOwnInfo(@RequestBody @Valid StudentSelfUpdateRequestDTO dto,
                                                       HttpServletRequest request){
        return ResponseEntity.ok(studentService.updateStudent(dto, request));
    }

    //* 2 - updateStudentById() - ADMIN, MANAGER
    @PutMapping("/update/{userId}")   // http://localhost:8080/student/update/2
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<UserResponseDTO>updateStudentById(
            @PathVariable Long userId,
            @RequestBody @Valid StudentCreateRequestDTO studentRequest) { // Create DTO ayni zamanda update edilebilecek bilgileri iceriyor.
        return ResponseEntity.ok(studentService.updateStudentById(userId,studentRequest));
    }

    @PostMapping("/select-lessons")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseEntity<String> selectLessons(@RequestBody @Valid StudentSelectLessonDTO dto, HttpServletRequest request) {
        return ResponseEntity.ok(studentService.selectLessons(dto, request));
    }

    @PatchMapping("/change-status/{studentId}/{newStatus}") //! Note: ideally newStatus should come in the request body, not as a path variable.
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserResponseDTO> patch(@PathVariable Long studentId, @PathVariable Boolean newStatus) {
        return ResponseEntity.ok(studentService.changeStatus(studentId, newStatus));
    }
}
