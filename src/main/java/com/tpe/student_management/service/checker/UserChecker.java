package com.tpe.student_management.service.checker;

import com.tpe.student_management.entity.user.User;
import com.tpe.student_management.enums.Role;
import com.tpe.student_management.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class UserChecker {
    public void checkIsAdvisor(User user){
        if (Boolean.FALSE.equals(user.getIsAdvisor())) {
            throw new BadRequestException("User is not an advisor.");
        }
    }

    public void checkRole(User user, Role role) {
        if (!user.getUserRole().getRole().equals(role)) {
            throw new BadRequestException("User is not a " + role.name());
        }
    }
}
