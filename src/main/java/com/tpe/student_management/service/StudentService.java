package com.tpe.student_management.service;

import com.tpe.student_management.dto.request.StudentCreateRequestDTO;
import com.tpe.student_management.dto.request.StudentSelectLessonDTO;
import com.tpe.student_management.dto.request.StudentSelfUpdateRequestDTO;
import com.tpe.student_management.dto.response.UserResponseDTO;
import com.tpe.student_management.entity.logic.Lesson;
import com.tpe.student_management.entity.user.User;
import com.tpe.student_management.enums.Role;
import com.tpe.student_management.repository.UserRepository;
import com.tpe.student_management.service.checker.UniquePropertyViolationChecker;
import com.tpe.student_management.service.checker.UserChecker;
import com.tpe.student_management.service.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final UserRepository userRepository;
    private final UniquePropertyViolationChecker uniquePropertyViolationChecker;
    private final UserService userService;
    private final UserChecker userChecker;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;
    private final LessonService lessonService;

    public UserResponseDTO save(StudentCreateRequestDTO dto) {
        uniquePropertyViolationChecker.checkProperties(dto.getUsername(), dto.getEmail(), dto.getPhoneNumber(), dto.getSsn());

        User foundUser = userService.findUserById(dto.getAdvisorTeacherId());
        userChecker.checkIsAdvisor(foundUser);

        User student = userMapper.mapStudentCreateRequestDTOToUser(dto);
        student.setAdvisorTeacherId(foundUser.getAdvisorTeacherId());
        student.setPassword(passwordEncoder.encode(dto.getPassword()));
        student.setUserRole(userRoleService.findUserRoleByRoleName(Role.STUDENT.name()));
        student.setStudentNumber(dto.getStudentNumber());
        student.setIsActive(true);

        return userMapper.mapUserToUserResponseDTO(userRepository.save(student));
    }

    public UserResponseDTO changeStatus(Long studentId, Boolean newStatus) {
        User foundUser = userService.findUserById(studentId);
        userChecker.checkRole(foundUser, Role.STUDENT);

        foundUser.setIsActive(newStatus);

        return userMapper.mapUserToUserResponseDTO(userRepository.save(foundUser));
    }

    public String updateStudent(StudentSelfUpdateRequestDTO dto, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");

        User student = userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException("No student found with given username: " + username)
        );


        //! Unique property validation
        uniquePropertyViolationChecker.checkProperties(student, dto);

        student.setMotherName(dto.getMotherName());
        student.setFatherName(dto.getFatherName());
        student.setBirthDate(dto.getDateOfBirth());
        student.setEmail(dto.getEmail());
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setBirthPlace(dto.getBirthPlace());
        student.setGender(dto.getGender());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setSsn(dto.getSsn());

        userRepository.save(student);

        return "Update successful.";
    }

    public UserResponseDTO updateStudentById(Long userId, @Valid StudentCreateRequestDTO studentRequest) {
        User user = userService.findUserById(userId);
        //! Throws exception if the given ID does not belong to a student.
        userChecker.checkRole(user, Role.STUDENT);
        //! Unique property validation
        uniquePropertyViolationChecker.checkProperties(user, studentRequest);

        user.setFirstName(studentRequest.getFirstName());
        user.setLastName(studentRequest.getLastName());
        user.setBirthDate(studentRequest.getDateOfBirth());
        user.setBirthPlace(studentRequest.getBirthPlace());
        user.setSsn(studentRequest.getSsn());
        user.setEmail(studentRequest.getEmail());
        user.setPhoneNumber(studentRequest.getPhoneNumber());
        user.setGender(studentRequest.getGender());
        user.setMotherName(studentRequest.getMotherName());
        user.setFatherName(studentRequest.getFatherName());
        user.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        user.setAdvisorTeacherId(studentRequest.getAdvisorTeacherId());

        User updatedStudent = userRepository.save(user);

        return userMapper.mapUserToUserResponseDTO(updatedStudent);
    }

    public String selectLessons(StudentSelectLessonDTO dto, HttpServletRequest request) {
        User user = userService.findUserByUsername((String) request.getAttribute("username"));
        List<Lesson> lessons = lessonService.findAllLessonsWithIds(dto.getLessonIds());

        user.setStudentLessons(lessons);

        userRepository.save(user);

        return "Lessons selected successfully.";
    }
}
