package com.tpe.student_management.dto.response;

import com.tpe.student_management.enums.Day;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Builder
public class LessonProgramSlotResponseDTO {
    private Long id;

    private Day day;

    private LocalTime startTime;

    private LocalTime endTime;

    private LessonResponseDTO lesson;
}
