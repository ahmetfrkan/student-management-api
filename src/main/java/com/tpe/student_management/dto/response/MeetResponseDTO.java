package com.tpe.student_management.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
public class MeetResponseDTO {
    private Long id;

    private String description;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private List<UserResponseDTO> students;

    private UserResponseDTO teacher;
}
