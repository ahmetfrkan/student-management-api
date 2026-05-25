package com.tpe.student_management.service;

import com.tpe.student_management.entity.user.UserRole;
import com.tpe.student_management.enums.Role;
import com.tpe.student_management.exception.BadRequestException;
import com.tpe.student_management.repository.UserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRole findUserRoleByRoleName(String roleName) {
        try {
            Role role = Role.valueOf(roleName);

            return userRoleRepository.findByRole(role).orElseThrow(
                    () -> new EntityNotFoundException("No role found: " + role.name())
            );
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Role name is invalid: " + roleName);
        }
    }
}
