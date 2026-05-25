package com.tpe.student_management.dto.response;

import com.tpe.student_management.entity.logic.EducationTerm;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonResponseDTO {
    private Long id;

    private String name;

    private Byte creditScore;

    private Boolean isMandatory;

    private EducationTerm term;

    private UserResponseDTO teacher;
}
