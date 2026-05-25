package com.tpe.student_management.dto.response;

import com.tpe.student_management.enums.LetterNote;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentInfoResponseDTO {
    private Long id;

    private Integer absence;

    private Double midtermGrade;

    private Double finalGrade;

    private Double averageGrade;

    private String infoNote;

    private LetterNote letterNote;

    private UserResponseDTO student;

    private String teacherFullName;

    private LessonResponseDTO lesson;
}
