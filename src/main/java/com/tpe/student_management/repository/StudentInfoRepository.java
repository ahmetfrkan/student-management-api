package com.tpe.student_management.repository;

import com.tpe.student_management.entity.logic.StudentInfo;
import com.tpe.student_management.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentInfoRepository extends JpaRepository<StudentInfo, Long> {
    Optional<StudentInfo> findByStudent_IdAndLesson_Id(Long studentId, Long lessonId);

    List<StudentInfo> findAllByStudent(User student);

    List<StudentInfo> findAllByTeacher(User teacher);

    Page<StudentInfo> findAllByStudent(User student, Pageable pageable);

    Page<StudentInfo> findAllByTeacher(User teacher, Pageable pageable);
}
