package com.tpe.student_management.controller;

import com.tpe.student_management.dto.request.MeetCreateRequestDTO;
import com.tpe.student_management.dto.response.MeetResponseDTO;
import com.tpe.student_management.service.MeetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meet")
@RequiredArgsConstructor
public class MeetController {
    private final MeetService meetService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ResponseEntity<MeetResponseDTO> saveMeet(@RequestBody @Valid MeetCreateRequestDTO dto, HttpServletRequest request) {
        return new ResponseEntity<>(meetService.save(dto, request) , HttpStatus.CREATED);
    }


    //* Find meet by id
    @GetMapping("/{id}")
    public ResponseEntity<MeetResponseDTO> getMeetById(@PathVariable Long id) {
        return ResponseEntity.ok(meetService.findById(id));
    }


    //* Find all meats of student, pageable
    //! Note: Same logic as teacher endpoint — only difference is which meet list is fetched based on role.
    @GetMapping("/get-by-user")
    @PreAuthorize("hasAnyAuthority('TEACHER', 'STUDENT')")
    public ResponseEntity<Page<MeetResponseDTO>> getMeetsOfUser(HttpServletRequest request,
                                                                @RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(required = false, defaultValue = "25") int size,
                                                                @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                                @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order) {
        return ResponseEntity.ok(meetService.findMeetsOfUser(request, page, size, sortBy, order));
    }
    //* Update meet
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ResponseEntity<MeetResponseDTO> updateMeet(@RequestBody MeetCreateRequestDTO dto, @PathVariable Long id, HttpServletRequest request) { // Same fields as create no separate DTO created
        return ResponseEntity.ok(meetService.updateMeet(id, dto, request));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public ResponseEntity<Void> deleteMeet(@PathVariable Long id, HttpServletRequest request) {
        meetService.delete(id, request);
        return ResponseEntity.noContent().build();
    }
}
