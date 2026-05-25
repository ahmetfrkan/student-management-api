package com.tpe.student_management.controller;

import com.tpe.student_management.dto.request.TeacherCreateRequestDTO;
import com.tpe.student_management.dto.response.UserResponseDTO;
import com.tpe.student_management.service.TeacherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserResponseDTO> saveTeacher(@RequestBody @Valid TeacherCreateRequestDTO dto) {
        return new ResponseEntity<>(teacherService.save(dto), HttpStatus.CREATED);
    }

    @GetMapping("/advisor/students")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ResponseEntity<List<UserResponseDTO>> getAllStudentsOfAdvisor(HttpServletRequest request) {
        return ResponseEntity.ok(teacherService.findStudentsOfAdvisor(request));
    }


    //* 1 - updateTeacherById() - ADMIN
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/update/{userId}")  // http://localhost:8080/user/update/1
    public ResponseEntity<UserResponseDTO>updateTeacherById(@RequestBody @Valid TeacherCreateRequestDTO teacherRequest,
                                                                @PathVariable Long userId){
        return ResponseEntity.ok(teacherService.updateTeacherById(teacherRequest, userId));
    }
    //* 2 - makeTeacherAdvisorById() - ADMIN, MANAGER
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @PatchMapping("/make-advisor/{teacherId}") // http://localhost:8080/teacher/make-advisor/1
    public ResponseEntity<String> makeTeacherAdvisorById (@PathVariable Long teacherId){
        return ResponseEntity.ok(teacherService.makeTeacherAdvisorById(teacherId));
    }
    //* 3 - makeAdvisorTeacherTeacherById(), ADMIN, MANAGER
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @PatchMapping("/unmake-advisor/{id}")// http://localhost:8080/teacher/unmake-advisor/1
    public ResponseEntity<String> makeAdvisorTeacherTeacherById(@PathVariable Long id){
        return ResponseEntity.ok(teacherService.makeAdvisorTeacherTeacherById(id));
    }

    //* 4 - getAllAdvisorTeachers(), All
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/advisors") // http://localhost:8080/teachers/advisors/
    public ResponseEntity<List<UserResponseDTO>> getAllAdvisorTeachers(){
        return ResponseEntity.ok(teacherService.getAllAdvisorTeachers());
    }
}
