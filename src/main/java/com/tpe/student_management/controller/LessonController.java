package com.tpe.student_management.controller;

import com.tpe.student_management.dto.request.LessonCreateRequestDTO;
import com.tpe.student_management.dto.request.LessonUpdateRequestDTO;
import com.tpe.student_management.dto.response.LessonResponseDTO;
import com.tpe.student_management.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<LessonResponseDTO> saveLesson(@RequestBody @Valid LessonCreateRequestDTO dto) {
        return new ResponseEntity<>(lessonService.save(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASSISTANT_MANAGER', 'TEACHER', 'STUDENT')")
    public ResponseEntity<LessonResponseDTO> getLessonById(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.findById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search-name")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASSISTANT_MANAGER', 'TEACHER', 'STUDENT')")
    public ResponseEntity<List<LessonResponseDTO>> getLessonsByName(@RequestParam String name) {
        return ResponseEntity.ok(lessonService.findAllByName(name));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASSISTANT_MANAGER', 'TEACHER', 'STUDENT')")
    public ResponseEntity<Page<LessonResponseDTO>> getAllLessons(@RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(required = false, defaultValue = "25") int size,
                                                                 @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                                 @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order) {
        return ResponseEntity.ok(lessonService.findAllLessons(page, size, sortBy, order));
    }

    @GetMapping("/search-id") //http://localhost:8080/lesson/search-id?lessonIds=1,56,7,23,5,83,7,56,23
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASSISTANT_MANAGER', 'TEACHER', 'STUDENT')")
    public ResponseEntity<List<LessonResponseDTO>> getAllLessonsWithIds(@RequestParam Set<Long> lessonIds) {
        return ResponseEntity.ok(lessonService.findAllById(lessonIds));
    }


    //* updateLessonById()
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<LessonResponseDTO> updateLessonById(@PathVariable Long id, @RequestBody @Valid LessonUpdateRequestDTO dto) {
        return ResponseEntity.ok(lessonService.updateLesson(id, dto));
    }
}
