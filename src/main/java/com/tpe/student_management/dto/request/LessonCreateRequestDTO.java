package com.tpe.student_management.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonCreateRequestDTO {
    private String name;

    private Byte creditScore;

    private Boolean isMandatory;

    private Long educationTermId;
}
