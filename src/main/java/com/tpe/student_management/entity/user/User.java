package com.tpe.student_management.entity.user;

import com.tpe.student_management.entity.logic.Lesson;
import com.tpe.student_management.entity.logic.Meet;
import com.tpe.student_management.entity.logic.StudentInfo;
import com.tpe.student_management.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "sm_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String ssn;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String birthPlace;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    private Boolean isBuiltIn; //! true false only for ADMIN role users. Null for other roles.

    private String motherName;

    private String fatherName;

    @Column(unique = true)
    private String studentNumber;

    private Long advisorTeacherId;

    private Boolean isAdvisor;  //! true false only for TEACHER role users. Null for other roles.

    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private UserRole userRole;

    @OneToMany(mappedBy = "student", cascade = CascadeType.DETACH)
    private List<StudentInfo> studentInfos;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.DETACH)
    private List<StudentInfo> teacherStudentInfos;

    @ManyToMany(mappedBy = "students")
    private List<Meet> studentMeets;

    @OneToMany(mappedBy = "advisorTeacher")
    private List<Meet> advisorTeacherMeets;

    @ManyToMany(mappedBy = "students")
    private List<Lesson> studentLessons;

    @OneToMany(mappedBy = "teacher")
    private List<Lesson> teacherLessons;
}
