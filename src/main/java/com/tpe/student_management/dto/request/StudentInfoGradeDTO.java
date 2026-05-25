package com.tpe.student_management.dto.request;

import com.tpe.student_management.enums.GradeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentInfoGradeDTO {
    private Long studentId;

    private Long lessonId;

    private Double grade;

    private GradeType gradeType;
}
