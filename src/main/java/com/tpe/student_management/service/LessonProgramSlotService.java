package com.tpe.student_management.service;

import com.tpe.student_management.dto.request.LessonProgramSlotCreateRequestDTO;
import com.tpe.student_management.dto.response.LessonProgramSlotResponseDTO;
import com.tpe.student_management.dto.response.LessonResponseDTO;
import com.tpe.student_management.entity.logic.Lesson;
import com.tpe.student_management.entity.logic.LessonProgramSlot;
import com.tpe.student_management.entity.user.User;
import com.tpe.student_management.exception.BadRequestException;
import com.tpe.student_management.messages.ErrorMessages;
import com.tpe.student_management.repository.LessonProgramSlotRepository;
import com.tpe.student_management.service.checker.UserChecker;
import com.tpe.student_management.service.mapper.LessonMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LessonProgramSlotService {
    private final LessonProgramSlotRepository lessonProgramSlotRepository;
    private final LessonService lessonService;
    private final LessonMapper lessonMapper;
    private final UserService userService;
    private final UserChecker userChecker;

    public LessonProgramSlotResponseDTO save(LessonProgramSlotCreateRequestDTO dto) {
        if (dto.getStartTime().isAfter(dto.getEndTime())) {
            throw new BadRequestException("Lesson start and end times are conflicting.");
        }

        Lesson lesson = lessonService.findLessonById(dto.getLessonId());

        LessonProgramSlot lessonProgramSlot = LessonProgramSlot.builder()
                .day(dto.getDay())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .lesson(lesson)
                .build();

        LessonProgramSlot savedLessonProgramSlot = lessonProgramSlotRepository.save(lessonProgramSlot);

        return mapLessonProgramSlotToLessonProgramSlotResponseDTO(savedLessonProgramSlot, lessonMapper.mapLessonToLessonResponseDTO(lesson));
    }

    private LessonProgramSlotResponseDTO mapLessonProgramSlotToLessonProgramSlotResponseDTO(LessonProgramSlot lessonProgramSlot, LessonResponseDTO dto) {
        return LessonProgramSlotResponseDTO.builder()
                .id(lessonProgramSlot.getId())
                .day(lessonProgramSlot.getDay())
                .startTime(lessonProgramSlot.getStartTime())
                .endTime(lessonProgramSlot.getEndTime())
                .lesson(dto)
                .build();
    }

    public List<LessonProgramSlotResponseDTO> findAll() {
        return lessonProgramSlotRepository.findAll().stream()
                .map(item -> mapLessonProgramSlotToLessonProgramSlotResponseDTO(item, lessonMapper.mapLessonToLessonResponseDTO(item.getLesson()))).toList();
    }

    private LessonProgramSlot findLessonProgramSlotById(Long id) {
        return lessonProgramSlotRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND, "LessonProgramSlot", "ID", id))
        );
    }

    public void delete(Long id) {
        lessonProgramSlotRepository.delete(findLessonProgramSlotById(id));
    }

    public Map<String, ?> findAllForStudent(HttpServletRequest request) {
        User student = userService.findUserByUsername((String)  request.getAttribute("username"));

        Map<String, Object> map = new HashMap<>();

        for (Lesson lesson : student.getStudentLessons()) {
            map.put(lesson.getName(), lesson.getLessonProgramSlots());
        }

        return map;
    }

    public Map<String,?> findAllForTeacher(HttpServletRequest request) {
        User teacher = userService.findUserByUsername((String)  request.getAttribute("username"));

        Map<String, Object> map = new HashMap<>();

        for (Lesson lesson : teacher.getTeacherLessons()) {
            map.put(lesson.getName(), lesson.getLessonProgramSlots());
        }

        return map;
    }
}
