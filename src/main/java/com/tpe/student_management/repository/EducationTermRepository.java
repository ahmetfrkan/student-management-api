package com.tpe.student_management.repository;

import com.tpe.student_management.entity.logic.EducationTerm;
import com.tpe.student_management.enums.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface EducationTermRepository extends JpaRepository<EducationTerm, Long> {
    @Query("SELECT COUNT(e) > 0 FROM EducationTerm e WHERE e.term = :term AND YEAR(e.startDate) = :year")
    boolean existsByTermAndStartDate_Year(Term term, int year);

    @Query("SELECT e FROM EducationTerm  e WHERE YEAR(e.startDate) = :startDateYear OR YEAR(e.endDate) = :endDateYear")
    List<EducationTerm> findByStartDate_YearOrEndDate_Year(int startDateYear, int endDateYear);
}
