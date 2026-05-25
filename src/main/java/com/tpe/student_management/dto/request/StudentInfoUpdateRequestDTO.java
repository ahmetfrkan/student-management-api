package com.tpe.student_management.dto.request;

import com.tpe.student_management.enums.LetterNote;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentInfoUpdateRequestDTO {
    private Integer absence;

    private Double midtermGrade;

    private Double finalGrade;

    private String infoNote;
}
