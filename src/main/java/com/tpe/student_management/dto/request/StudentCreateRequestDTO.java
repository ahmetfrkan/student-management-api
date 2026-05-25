package com.tpe.student_management.dto.request;

import com.tpe.student_management.dto.request.abstracts.BaseUserRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NotNull
@SuperBuilder
public class StudentCreateRequestDTO extends BaseUserRequestDTO {
    @NotBlank(message = "Mother name is required.")
    private String motherName;

    @NotBlank(message = "Father name is required.")
    private String fatherName;

    @NotNull(message = "Advisor teacher id is required.")
    private Long advisorTeacherId;

    @NotBlank(message = "Student number is required.")
    private String studentNumber;
}
