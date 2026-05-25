package com.tpe.student_management.dto.request;

import com.tpe.student_management.enums.Day;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class LessonProgramSlotCreateRequestDTO {
    private Day day;

    private LocalTime startTime;

    private LocalTime endTime;

    private Long lessonId;
}
