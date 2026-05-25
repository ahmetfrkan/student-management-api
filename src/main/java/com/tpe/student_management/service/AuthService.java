package com.tpe.student_management.service;

import com.tpe.student_management.dto.request.PasswordChangeRequestDTO;
import com.tpe.student_management.dto.request.UserLoginDTO;
import com.tpe.student_management.dto.response.LoginResponseDTO;
import com.tpe.student_management.entity.user.User;
import com.tpe.student_management.exception.InvalidPasswordException;
import com.tpe.student_management.exception.UnallowedOperationException;
import com.tpe.student_management.repository.UserRepository;
import com.tpe.student_management.security.jwt.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JWTUtils jWTUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public Map<String,?> login(UserLoginDTO dto, HttpServletResponse response) {
        //! This single line handles both user existence check and password validation.
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

        User user = userService.findUserByUsername(dto.getUsername());

        String accessToken = jWTUtils.generateAccessToken(dto.getUsername(), user.getUserRole().getRole().name());
        String refreshToken = jWTUtils.generateRefreshToken(dto.getUsername(), user.getUserRole().getRole().name());

        //! The response header will contain a refreshToken cookie upon successful login.
        setRefreshTokenCookie(response, refreshToken);

        return Map.of("message", "Login success",
                "accessToken", accessToken,
                "user", LoginResponseDTO.builder().id(user.getId())
                        .username(user.getUsername())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .gender(user.getGender())
                        .userRole(user.getUserRole())
                        .isActive(user.getIsActive())
                        .isAdvisor(user.getIsAdvisor())
                        .build()
        );
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // false in development — set to true in production
                .path("/api/auth/refresh")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public String changePassword(PasswordChangeRequestDTO dto, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = userService.findUserByUsername(username);

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Old password does not match our records.");
        }

        //! Note: Even built-in users should change their passwords periodically in a real system.
        //! This check exists as a demonstration only.
        if (Boolean.TRUE.equals(user.getIsBuiltIn())) {
            throw new UnallowedOperationException("Built-in users cannot be updated.");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        userRepository.save(user);

        return "Password updated successfully.";
    }
}
