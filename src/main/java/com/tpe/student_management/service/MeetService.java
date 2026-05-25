package com.tpe.student_management.service;

import com.tpe.student_management.dto.request.MeetCreateRequestDTO;
import com.tpe.student_management.dto.response.MeetResponseDTO;
import com.tpe.student_management.entity.logic.Meet;
import com.tpe.student_management.entity.user.User;
import com.tpe.student_management.enums.Role;
import com.tpe.student_management.exception.BadRequestException;
import com.tpe.student_management.exception.ConflictException;
import com.tpe.student_management.messages.ErrorMessages;
import com.tpe.student_management.repository.MeetRepository;
import com.tpe.student_management.service.checker.UserChecker;
import com.tpe.student_management.service.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetService {
    private final MeetRepository meetRepository;
    private final UserService userService;
    private final UserChecker userChecker;
    private final UserMapper userMapper;

    public MeetResponseDTO save(MeetCreateRequestDTO dto, HttpServletRequest request) {
        User teacher = userService.findUserByUsername((String) request.getAttribute("username"));
        userChecker.checkIsAdvisor(teacher);
        checkMeetDateTimeConflict(teacher.getId(), dto.getDate(), dto.getStartTime(), dto.getEndTime());

        List<User> students = findStudentsAndAddToList(dto);

        Meet meet = new Meet();

        meet.setDescription(dto.getDescription());
        meet.setDate(dto.getDate());
        meet.setStartTime(dto.getStartTime());
        meet.setEndTime(dto.getEndTime());
        meet.setStudents(students);
        meet.setAdvisorTeacher(teacher);

        return mapMeetToMeetResponseDTO(meetRepository.save(meet));
    }

    private MeetResponseDTO mapMeetToMeetResponseDTO(Meet meet) {
        return MeetResponseDTO.builder()
                .id(meet.getId())
                .description(meet.getDescription())
                .date(meet.getDate())
                .startTime(meet.getStartTime())
                .endTime(meet.getEndTime())
                .students(meet.getStudents().stream().map(userMapper::mapUserToUserResponseDTO).toList())
                .teacher(userMapper.mapUserToUserResponseDTO(meet.getAdvisorTeacher()))
                .build();
    }

    private void checkMeetDateTimeConflict(Long userId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        if (meetRepository.dateTimeConflicts(userId, date, startTime, endTime)) {
            throw new ConflictException("Meet dates are conflicting with existing ones.");
        }
    }

    private Meet findMeetById(Long id) {
        return meetRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND, "Meet", "ID", id))
        );
    }

    public void delete(Long id, HttpServletRequest request) {
        Meet meet = findMeetById(id);

        User teacher = userService.findUserByUsername((String) request.getAttribute("username"));
        userChecker.checkIsAdvisor(teacher);

        checkIfTeacherOwnsMeet(meet, teacher);

        meetRepository.delete(meet);
    }

    public MeetResponseDTO findById(Long id) {
        return mapMeetToMeetResponseDTO(findMeetById(id));
    }

    private static void checkIfTeacherOwnsMeet(Meet meet, User teacher) {
        if (!meet.getAdvisorTeacher().equals(teacher)) {
            throw new BadRequestException("This meet does not belong to current advisor teacher.");
        }
    }

    public Page<MeetResponseDTO> findMeetsOfUser(HttpServletRequest request, int page, int size, String sortBy, Sort.Direction order) {
        User user = userService.findUserByUsername((String) request.getAttribute("username"));

        
        Pageable pageable = PageRequest.of(page - 1, size, order, sortBy);
        
        if (user.getUserRole().getRole().equals(Role.TEACHER)) {
            return meetRepository.findAllByAdvisorTeacher(user, pageable).map(this::mapMeetToMeetResponseDTO);
        } else {
            return meetRepository.findAllByStudentsContaining(user, pageable).map(this::mapMeetToMeetResponseDTO);
        }
    }

    public MeetResponseDTO updateMeet(Long id, MeetCreateRequestDTO dto, HttpServletRequest request) {
        User teacher = userService.findUserByUsername((String) request.getAttribute("username"));
        userChecker.checkIsAdvisor(teacher);

        Meet meet = findMeetById(id);
        checkIfTeacherOwnsMeet(meet, teacher);

        meet.setDescription(dto.getDescription());
        meet.setDate(dto.getDate());
        meet.setStartTime(dto.getStartTime());
        meet.setEndTime(dto.getEndTime());

        List<User> students = findStudentsAndAddToList(dto);
        meet.setStudents(students);

        return mapMeetToMeetResponseDTO(meetRepository.save(meet));
    }

    private List<User> findStudentsAndAddToList(MeetCreateRequestDTO dto) {
        List<User> students = new ArrayList<>();

        for (Long studentId : dto.getStudentIds()) {
            User student = userService.findUserById(studentId);
            userChecker.checkRole(student, Role.STUDENT);

            checkMeetDateTimeConflict(student.getId(), dto.getDate(), dto.getStartTime(), dto.getEndTime());
            students.add(student);
        }
        return students;
    }
}
