package com.tpe.student_management.repository;

import com.tpe.student_management.entity.user.User;
import com.tpe.student_management.entity.user.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsBySsn(String ssn);

    Page<User> findAllByUserRole(UserRole userRole, Pageable pageable);

    List<User> findAllByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    List<User> findAllByAdvisorTeacherId(Long advisorTeacherId);

    List<User> findAllByIsAdvisor(Boolean isAdvisor);
}
