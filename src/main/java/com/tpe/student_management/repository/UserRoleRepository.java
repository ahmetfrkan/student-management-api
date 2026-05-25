package com.tpe.student_management.repository;

import com.tpe.student_management.entity.user.UserRole;
import com.tpe.student_management.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByRole(Role role);
}
