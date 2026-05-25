package com.tpe.student_management.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class StudentSelectLessonDTO {
    @NotEmpty
    private Set<Long> lessonIds;
}
