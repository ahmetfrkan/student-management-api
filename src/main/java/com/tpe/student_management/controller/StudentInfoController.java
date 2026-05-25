package com.tpe.student_management.controller;

import com.tpe.student_management.dto.request.StudentInfoPatchRequestDTO;
import com.tpe.student_management.dto.request.StudentInfoUpdateRequestDTO;
import com.tpe.student_management.dto.response.StudentInfoResponseDTO;
import com.tpe.student_management.service.StudentInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student-info")
@RequiredArgsConstructor
public class StudentInfoController {
    private final StudentInfoService studentInfoService;

    @PatchMapping("/set-grades")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ResponseEntity<String> setGradesOfStudents(@RequestBody @Valid StudentInfoPatchRequestDTO dto, HttpServletRequest request) {
        return ResponseEntity.ok(studentInfoService.setGradesOfStudents(dto, request));
    }


    @GetMapping
    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER')")
    public ResponseEntity<Page<StudentInfoResponseDTO>> getAllStudentInfos(HttpServletRequest request,
                                                                           @RequestParam(defaultValue = "1") int page,
                                                                           @RequestParam(required = false, defaultValue = "25") int size,
                                                                           @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                                           @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order) {
        return ResponseEntity.ok(studentInfoService.findAll(request, page, size, sortBy, order));
    }

    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER')")
    public ResponseEntity<List<StudentInfoResponseDTO>> getStudentInfosOfTeacher(HttpServletRequest request) {
        return ResponseEntity.ok(studentInfoService.findStudentInfosOfUser(request));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    public ResponseEntity<String> updateStudentInfo(@RequestBody @Valid StudentInfoUpdateRequestDTO dto, @PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(studentInfoService.update(id, dto, request));
    }

}
