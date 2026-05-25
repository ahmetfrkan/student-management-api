package com.tpe.student_management.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tpe.student_management.enums.Term;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EducationTermRequestDTO {

    @NotNull(message = "Education Term must not be empty")
    private Term term;

    @NotNull(message ="Start Date must not be empty")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message ="End Date must not be empty")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message ="Last Registration Date must not be empty")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate lastRegistrationDate;
}