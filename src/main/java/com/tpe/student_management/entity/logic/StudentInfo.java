package com.tpe.student_management.entity.logic;

import com.tpe.student_management.entity.user.User;
import com.tpe.student_management.enums.LetterNote;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "sm_student_info")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentInfo {
    //! Entity representing a students academic performance record for a specific lesson.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer absence;

    private Double midtermGrade;

    private Double finalGrade;

    private Double averageGrade;

    private String infoNote;

    private LetterNote letterNote;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

}
