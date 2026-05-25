package com.tpe.student_management.repository;

import com.tpe.student_management.entity.logic.Meet;
import com.tpe.student_management.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;

public interface MeetRepository extends JpaRepository<Meet, Long> {

    @Query("SELECT count(m) > 0 FROM Meet m WHERE m.date = :date " +
            "AND (m.advisorTeacher.id = :userId OR EXISTS (SELECT s FROM m.students s WHERE s.id = :userId))" +
            "AND (:startTime < m.endTime AND :endTime > m.startTime)")
    boolean dateTimeConflicts(Long userId, LocalDate date, LocalTime startTime, LocalTime endTime);

    Page<Meet> findAllByAdvisorTeacher(User advisorTeacher, Pageable pageable);
    Page<Meet> findAllByStudentsContaining(User student, Pageable pageable);
}
