package com.tpe.student_management.entity.logic;

import com.tpe.student_management.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "sm_lesson")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Byte creditScore;

    private Boolean isMandatory;

    @ManyToOne
    @JoinColumn(name = "education_term_id")
    private EducationTerm term;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<LessonProgramSlot> lessonProgramSlots;

    @ManyToMany
    @JoinTable(
            name = "sm_student_lesson",
            joinColumns = @JoinColumn(name = "lesson_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<User> students;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;
}
