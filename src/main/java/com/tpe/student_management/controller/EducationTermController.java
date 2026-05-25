package com.tpe.student_management.controller;

import com.tpe.student_management.dto.request.EducationTermRequestDTO;
import com.tpe.student_management.dto.response.EducationTermResponseDTO;
import com.tpe.student_management.service.EducationTermService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/education-term")
@RequiredArgsConstructor
public class EducationTermController {
    private final EducationTermService educationTermService;

    @PostMapping("/save")// http://localhost:8080/educationTerms/save + JSON + POST
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<EducationTermResponseDTO> saveEducationTerm(@RequestBody @Valid
                                                                      EducationTermRequestDTO educationTermRequest){
        return new ResponseEntity<>(educationTermService.saveEducationTerm(educationTermRequest), HttpStatusCode.valueOf(201));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @PutMapping("/update/{id}")// http://localhost:8080/educationTerms/update/1 + JSON
    public ResponseEntity<EducationTermResponseDTO>updateEducationTerm(@PathVariable Long id,
                                                                        @RequestBody @Valid EducationTermRequestDTO educationTermRequest ){
        return ResponseEntity.ok(educationTermService.updateEducationTerm(id,educationTermRequest));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN, MANAGER, ASSISTANT_MANAGER, TEACHER, STUDENT')")
    public ResponseEntity<EducationTermResponseDTO> getEducationTermById(@PathVariable Long id) {
        return ResponseEntity.ok(educationTermService.findById(id));
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<EducationTermResponseDTO>> getAllEducationTerms() {
        return ResponseEntity.ok(educationTermService.findAllEducationTerms());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducationTerm(@PathVariable Long id) {
        educationTermService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
