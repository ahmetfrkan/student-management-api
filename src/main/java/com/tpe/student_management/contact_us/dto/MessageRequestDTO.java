package com.tpe.student_management.contact_us.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequestDTO {

    @NotBlank(message = "First name is required.")
    @Size(min = 2, max = 32, message = "First name must be between {min}-{max} characters.")
    @Pattern(regexp = "^[A-Za-z'-]+$", message = "First name must only consist letters or single tick.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(min = 2, max = 32, message = "Last name must be between {min}-{max} characters.")
    @Pattern(regexp = "^[A-Za-z'-]", message = "Last name must only consist letters or single tick.")
    private String lastName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email structure.", regexp = "^([A-Za-z0-9.\\-_#]+@[A-Za-z0-9]+\\.[A-Za-z]{2,})$")
    private String email; //Note: basic @Email only checks a@a.a format. TLD minimum 2 chars is enforced via custom regex.

    @NotBlank(message = "Subject is required.")
    @Size(min = 6, max = 64, message = "Subject must be between {min}-{max} characters long.")
    private String subject;

    @NotBlank(message = "Message is required.")
    @Size(min = 8, max = 2000, message = "Message must be between {min}-{max} characters long.")
    private String message;
}