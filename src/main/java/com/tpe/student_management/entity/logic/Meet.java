package com.tpe.student_management.entity.logic;

import com.tpe.student_management.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "sm_meet")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Meet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    @ManyToMany
    @JoinTable(
            name = "sm_meet_student",
            joinColumns = @JoinColumn(name = "meet_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<User> students;

    @ManyToOne
    @JoinColumn(name = "advisor_teacher_id")
    private User advisorTeacher;
}
