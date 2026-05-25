package com.tpe.student_management.contact_us.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponseDTO {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String subject;

    private String message;
}
