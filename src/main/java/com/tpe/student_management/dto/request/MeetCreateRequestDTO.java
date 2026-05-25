package com.tpe.student_management.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class MeetCreateRequestDTO {
    private String description;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private List<Long> studentIds;
}
