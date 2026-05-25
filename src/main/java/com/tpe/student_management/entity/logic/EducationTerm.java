package com.tpe.student_management.entity.logic;

import com.tpe.student_management.enums.Term;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "sm_education_term")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EducationTerm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Term term;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate lastRegistrationDate;
}
