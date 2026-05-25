package com.tpe.student_management.controller;

import com.tpe.student_management.dto.request.PasswordChangeRequestDTO;
import com.tpe.student_management.dto.request.UserLoginDTO;
import com.tpe.student_management.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, ?>> login(@RequestBody @Valid UserLoginDTO dto, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(dto, response));
    }

    @PatchMapping("/update-password")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASSISTANT_MANAGER', 'TEACHER', 'STUDENT')")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid PasswordChangeRequestDTO dto, HttpServletRequest request) {
        return ResponseEntity.ok(authService.changePassword(dto, request));
    }
}
