package com.tpe.student_management.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tpe.student_management.entity.user.UserRole;
import com.tpe.student_management.enums.Gender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String ssn;
    private LocalDate birthDate;
    private String birthPlace;
    private String phoneNumber;
    private Gender gender;
    private UserRole userRole;
    private Boolean isActive;
    private Boolean isAdvisor;
    private Boolean isBuiltIn;
    private String motherName;
    private String fatherName;
    private String studentNumber;
    private Long advisorTeacherId;
}
