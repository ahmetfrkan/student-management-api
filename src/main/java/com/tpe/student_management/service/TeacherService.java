package com.tpe.student_management.service;

import com.tpe.student_management.dto.request.TeacherCreateRequestDTO;
import com.tpe.student_management.dto.response.UserResponseDTO;
import com.tpe.student_management.entity.logic.Lesson;
import com.tpe.student_management.entity.user.User;
import com.tpe.student_management.enums.Role;
import com.tpe.student_management.exception.ConflictException;
import com.tpe.student_management.messages.ErrorMessages;
import com.tpe.student_management.repository.LessonRepository;
import com.tpe.student_management.repository.UserRepository;
import com.tpe.student_management.service.checker.UniquePropertyViolationChecker;
import com.tpe.student_management.service.checker.UserChecker;
import com.tpe.student_management.service.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final UserRepository userRepository;
    private final UniquePropertyViolationChecker uniquePropertyViolationChecker;
    private final LessonRepository lessonRepository;
    private final UserMapper userMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final UserChecker userChecker;
    private final UserService userService;
    private final LessonService lessonService;

    @Transactional
    public UserResponseDTO save(TeacherCreateRequestDTO dto) {
        uniquePropertyViolationChecker.checkProperties(dto.getUsername(), dto.getEmail(), dto.getPhoneNumber(), dto.getSsn());

        List<Lesson> foundLessons = lessonService.findAllLessonsWithIds(dto.getLessonIds());

        User user = userMapper.mapTeacherCreateRequestDTOToUser(dto);
        user.setUserRole(userRoleService.findUserRoleByRoleName(Role.TEACHER.name()));
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User teacher = userRepository.save(user);

        for (Lesson l : foundLessons) {
            l.setTeacher(teacher);
        }

        lessonRepository.saveAll(foundLessons);

        return userMapper.mapUserToUserResponseDTO(teacher);
    }

    public List<UserResponseDTO> findStudentsOfAdvisor(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");


        User teacher = userService.findUserByUsername(username);

        userChecker.checkIsAdvisor(teacher);

        return userRepository.findAllByAdvisorTeacherId(teacher.getId())
                .stream().map(userMapper::mapUserToUserResponseDTO).toList();
    }

    @Transactional
    public UserResponseDTO updateTeacherById(TeacherCreateRequestDTO teacherRequest, Long userId) {
        User user = userService.findUserById(userId);
        userChecker.checkRole(user, Role.TEACHER);

        List<Lesson> foundLessons = lessonService.findAllLessonsWithIds(teacherRequest.getLessonIds());

        uniquePropertyViolationChecker.checkProperties(user, teacherRequest);

        User updatedTeacher = userMapper.mapTeacherRequestToUpdatedUser(teacherRequest, userId);

        updatedTeacher.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));

        User savedTeacher = userRepository.save(updatedTeacher);

        for (Lesson l : foundLessons) {
            l.setTeacher(savedTeacher);
        }

        lessonRepository.saveAll(foundLessons);

        updatedTeacher.setUserRole(userRoleService.findUserRoleByRoleName(Role.TEACHER.name()));

        return userMapper.mapUserToUserResponseDTO(savedTeacher);
    }

    public String makeTeacherAdvisorById(Long teacherId) {
        User teacher = userService.findUserById(teacherId);

        userChecker.checkRole(teacher, Role.TEACHER);

        if(Boolean.TRUE.equals(teacher.getIsAdvisor())) { // condition : teacher.getIsAdvisor()
            throw new ConflictException(ErrorMessages.TEACHER_ALREADY_ADVISOR);
        }

        teacher.setIsAdvisor(Boolean.TRUE);
        userRepository.save(teacher);

        return "Update successful.";
    }

    public String makeAdvisorTeacherTeacherById(Long id) {
        User teacher = userService.findUserById(id);

        userChecker.checkRole(teacher, Role.TEACHER);

        if(Boolean.FALSE.equals(teacher.getIsAdvisor())) { // condition : !teacher.getIsAdvisor()
            throw new ConflictException(ErrorMessages.TEACHER_ALREADY_NOT_ADVISOR);
        }

        teacher.setIsAdvisor(Boolean.FALSE);
        userRepository.save(teacher);

        return "Update successful.";
    }

    public List<UserResponseDTO> getAllAdvisorTeachers() {
        return userRepository.findAllByIsAdvisor(Boolean.TRUE) // Derived query
                .stream()
                .map(userMapper::mapUserToUserResponseDTO)
                .collect(Collectors.toList());
    }
}
