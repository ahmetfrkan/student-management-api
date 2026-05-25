package com.tpe.student_management.controller;

import com.tpe.student_management.dto.request.LessonProgramSlotCreateRequestDTO;
import com.tpe.student_management.dto.response.LessonProgramSlotResponseDTO;
import com.tpe.student_management.service.LessonProgramSlotService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lesson-program")
@RequiredArgsConstructor
public class LessonProgramSlotController {
    private final LessonProgramSlotService lessonProgramSlotService;

    @PostMapping("/save-slot")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<LessonProgramSlotResponseDTO> saveLessonProgramSlot(@RequestBody @Valid LessonProgramSlotCreateRequestDTO dto) {
        return new ResponseEntity<>(lessonProgramSlotService.save(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<LessonProgramSlotResponseDTO>> getAllLessonProgramSlots() {
        return ResponseEntity.ok(lessonProgramSlotService.findAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> deleteLessonProgramSlot(@PathVariable Long id) {
        lessonProgramSlotService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-by-student")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseEntity<Map<String, ?>> getLessonProgramSlotsOfStudent(HttpServletRequest request) {
        return ResponseEntity.ok(lessonProgramSlotService.findAllForStudent(request));
    }

    @GetMapping("/get-by-teacher")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ResponseEntity<Map<String, ?>> getLessonProgramSlotsOfTeacher(HttpServletRequest request) {
        return ResponseEntity.ok(lessonProgramSlotService.findAllForTeacher(request));

    }
}
