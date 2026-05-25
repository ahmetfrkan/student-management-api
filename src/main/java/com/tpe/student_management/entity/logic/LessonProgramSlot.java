package com.tpe.student_management.entity.logic;

import com.tpe.student_management.enums.Day;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "sm_lesson_program")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonProgramSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Day day;

    private LocalTime startTime;

    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}
