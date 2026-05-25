package com.tpe.student_management.service;

import com.tpe.student_management.dto.request.StudentInfoGradeDTO;
import com.tpe.student_management.dto.request.StudentInfoPatchRequestDTO;
import com.tpe.student_management.dto.request.StudentInfoUpdateRequestDTO;
import com.tpe.student_management.dto.response.StudentInfoResponseDTO;
import com.tpe.student_management.entity.logic.StudentInfo;
import com.tpe.student_management.entity.user.User;
import com.tpe.student_management.enums.GradeType;
import com.tpe.student_management.enums.LetterNote;
import com.tpe.student_management.enums.Role;
import com.tpe.student_management.exception.BadRequestException;
import com.tpe.student_management.messages.ErrorMessages;
import com.tpe.student_management.repository.StudentInfoRepository;
import com.tpe.student_management.service.mapper.LessonMapper;
import com.tpe.student_management.service.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentInfoService {
    private final StudentInfoRepository studentInfoRepository;
    private final UserService userService;
    private final LessonMapper lessonMapper;
    private final UserMapper userMapper;

    @Value("${variable.midterm_impact_percentage}")
    private Double midtermWeight;
    
    @Value("${variable.final_impact_percentage}")
    private Double finalWeight;

    public String setGradesOfStudents(StudentInfoPatchRequestDTO dto, HttpServletRequest request) {
        User teacher = userService.findUserByUsername((String) request.getAttribute("username"));

        List<StudentInfo> studentInfos = new ArrayList<>();

        for (StudentInfoGradeDTO gradeDTO : dto.getGrades()) {
            StudentInfo studentInfo = studentInfoRepository.findByStudent_IdAndLesson_Id(gradeDTO.getStudentId(), gradeDTO.getLessonId())
                    .orElseThrow(() -> new EntityNotFoundException("No StudentInfo found for given Student ID or Lesson ID. Student: " + gradeDTO.getStudentId() + " Lesson: " + gradeDTO.getLessonId()));

            if (!studentInfo.getTeacher().equals(teacher)) {
                throw new BadRequestException("This teacher is not the teacher of lesson: " + gradeDTO.getLessonId());
            }
            
            if (gradeDTO.getGradeType().equals(GradeType.MIDTERM)) {
                studentInfo.setMidtermGrade(gradeDTO.getGrade());
            } else {
                studentInfo.setFinalGrade(gradeDTO.getGrade());
                studentInfo.setAverageGrade((studentInfo.getMidtermGrade() * midtermWeight) + (studentInfo.getFinalGrade() * finalWeight));
                studentInfo.setLetterNote(getLetterGrades(studentInfo.getAverageGrade()));
            }

            studentInfos.add(studentInfo);
        }

        studentInfoRepository.saveAll(studentInfos);

        return "Grades are set successfully.";
    }

    private static LetterNote getLetterGrades(Double average) {
        if (average < 50.00) {
            return LetterNote.FF;
        } else if (average < 60.00) {
            return LetterNote.DD;
        } else if (average < 65.00) {
            return LetterNote.DC;
        } else if (average < 70.00) {
            return LetterNote.CC;
        } else if (average < 80.00) {
            return LetterNote.CB;
        } else if (average < 90.00) {
            return LetterNote.BB;
        } else if (average < 95.00) {
            return LetterNote.BA;
        } else {
            return LetterNote.AA;
        }
    }

    private StudentInfoResponseDTO mapStudentInfoToStudentInfoResponseDTO(StudentInfo studentInfo) {
        return StudentInfoResponseDTO.builder()
                .id(studentInfo.getId())
                .absence(studentInfo.getAbsence())
                .midtermGrade(studentInfo.getMidtermGrade())
                .finalGrade(studentInfo.getFinalGrade())
                .averageGrade(studentInfo.getAverageGrade())
                .infoNote(studentInfo.getInfoNote())
                .letterNote(studentInfo.getLetterNote())
                .student(userMapper.mapUserToUserResponseDTO(studentInfo.getStudent()))
                .teacherFullName(studentInfo.getTeacher().getFirstName() + " " + studentInfo.getTeacher().getLastName())
                .lesson(lessonMapper.mapLessonToLessonResponseDTO(studentInfo.getLesson()))
                .build();
    }

    public List<StudentInfoResponseDTO> findStudentInfosOfUser(HttpServletRequest request) {
        User user = userService.findUserByUsername((String) request.getAttribute("username"));

        List<StudentInfo> studentInfos;

        if (user.getUserRole().getRole().equals(Role.TEACHER)) {
            studentInfos = studentInfoRepository.findAllByTeacher(user);
        } else {
            studentInfos = studentInfoRepository.findAllByStudent(user);
        }

        return studentInfos.stream().map(this::mapStudentInfoToStudentInfoResponseDTO).toList();
    }

    private StudentInfo findStudentInfoById(Long id) {
        return studentInfoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND, "StudentInfo", "ID", id))
        );
    }

    public String update(Long id, StudentInfoUpdateRequestDTO dto, HttpServletRequest request) {
        StudentInfo foundStudentInfo = findStudentInfoById(id);
        User user = userService.findUserByUsername((String) request.getAttribute("username"));

        if (user.getUserRole().getRole().equals(Role.TEACHER) && !foundStudentInfo.getTeacher().equals(user)) {
            throw new BadRequestException("Teacher does not own this StudentInfo.");
        }

        foundStudentInfo.setAbsence(dto.getAbsence());
        foundStudentInfo.setMidtermGrade(dto.getMidtermGrade());
        foundStudentInfo.setFinalGrade(dto.getFinalGrade());
        foundStudentInfo.setAverageGrade(midtermWeight * dto.getMidtermGrade() + finalWeight * dto.getFinalGrade());
        foundStudentInfo.setInfoNote(dto.getInfoNote());
        foundStudentInfo.setLetterNote(getLetterGrades(foundStudentInfo.getAverageGrade()));

        studentInfoRepository.save(foundStudentInfo);

        return "Update successful.";
    }

    public Page<StudentInfoResponseDTO> findAll(HttpServletRequest request, int page, int size, String sortBy, Sort.Direction order) {
        User user = userService.findUserByUsername((String) request.getAttribute("username"));

        Pageable pageable = PageRequest.of(page - 1, size, order, sortBy);

        if (user.getUserRole().getRole().equals(Role.TEACHER)) {
            return studentInfoRepository.findAllByTeacher(user, pageable).map(this::mapStudentInfoToStudentInfoResponseDTO);
        } else {
            return studentInfoRepository.findAllByStudent(user, pageable).map(this::mapStudentInfoToStudentInfoResponseDTO);

        }
    }
}
