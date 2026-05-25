package com.tpe.student_management.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentInfoPatchRequestDTO {
    private List<StudentInfoGradeDTO> grades;
}
