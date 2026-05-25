package com.tpe.student_management.config;

import com.tpe.student_management.entity.user.User;
import com.tpe.student_management.entity.user.UserRole;
import com.tpe.student_management.enums.Gender;
import com.tpe.student_management.enums.Role;
import com.tpe.student_management.repository.UserRepository;
import com.tpe.student_management.repository.UserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        //1 - Roles must exist in DB
        initializeRole(Role.ADMIN);
        initializeRole(Role.MANAGER);
        initializeRole(Role.ASSISTANT_MANAGER);
        initializeRole(Role.TEACHER);
        initializeRole(Role.STUDENT);

        //2 - Admin user must exist in DB
        if (userRepository.findByUsername("Admin").isEmpty()) {
            UserRole adminRole = userRoleRepository.findByRole(Role.ADMIN).orElseThrow(
                    () -> new EntityNotFoundException("No role found: ADMIN")
            );

            User admin = new User();

            admin.setUsername("Admin");
            admin.setFirstName("System");
            admin.setLastName("Administrator");
            admin.setEmail("admin@studentmanagement.com");
            admin.setPassword(passwordEncoder.encode("admin123123"));
            admin.setSsn("NO_SSN");
            admin.setBirthDate(LocalDate.of(1970, 1, 1));
            admin.setBirthPlace("NO_BIRTHPLACE");
            admin.setPhoneNumber("+11111111111");
            admin.setIsBuiltIn(true);
            admin.setGender(Gender.MALE);
            admin.setUserRole(adminRole);

            userRepository.save(admin);
        }
    }

    private void initializeRole(Role role) {
        if (userRoleRepository.findByRole(role).isEmpty()) {
            UserRole userRole = new UserRole();
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        }
    }
}
