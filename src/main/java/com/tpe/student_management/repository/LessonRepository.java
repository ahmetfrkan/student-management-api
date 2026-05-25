package com.tpe.student_management.repository;

import com.tpe.student_management.entity.logic.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByNameContainsIgnoreCase(String name);
}
