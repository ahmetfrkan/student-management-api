package com.tpe.student_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequestDTO {
    @NotBlank(message = "Old password is required")
    @Size(min = 8, max = 64, message = "Old password must be between {min}-{max} characters.")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 64, message = "New password must be between {min}-{max} characters.")
    private String newPassword;
}
