package com.tpe.student_management.dto.request;

import com.tpe.student_management.dto.request.abstracts.BaseUserRequestDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TeacherCreateRequestDTO extends BaseUserRequestDTO {
    @NotEmpty(message = "At least one lesson is required.")
    private Set<Long> lessonIds;

    @NotNull(message = "Boolean:isAdvisor is required.")
    private Boolean isAdvisor;
}
