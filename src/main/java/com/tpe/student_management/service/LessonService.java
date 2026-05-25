package com.tpe.student_management.service;

import com.tpe.student_management.dto.request.LessonCreateRequestDTO;
import com.tpe.student_management.dto.request.LessonUpdateRequestDTO;
import com.tpe.student_management.dto.response.LessonResponseDTO;
import com.tpe.student_management.entity.logic.Lesson;
import com.tpe.student_management.entity.user.User;
import com.tpe.student_management.enums.Role;
import com.tpe.student_management.exception.BadRequestException;
import com.tpe.student_management.messages.ErrorMessages;
import com.tpe.student_management.repository.LessonRepository;
import com.tpe.student_management.service.checker.UserChecker;
import com.tpe.student_management.service.mapper.LessonMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final UserService userService;
    private final UserChecker userChecker;
    private final LessonMapper lessonMapper;
    private final EducationTermService educationTermService;

    public List<Lesson> findAllLessonsWithIds(Set<Long> lessonIds) {
        List<Lesson> lessons = lessonRepository.findAllById(lessonIds);

        List<Long> foundIds = lessons.stream().map(Lesson::getId).toList();

        List<Long> missingIds = lessonIds.stream().filter(id -> !foundIds.contains(id)).toList();

        if (!missingIds.isEmpty()) {
            throw new BadRequestException("Some lessons are missing: " + missingIds);
        }

        return lessons;
    }

    public LessonResponseDTO save(LessonCreateRequestDTO dto) {
        Lesson lesson = lessonMapper.mapLessonCreateRequestDTOToLesson(dto);

        lesson.setTerm(educationTermService.findEducationTermById(dto.getEducationTermId()));

        return lessonMapper.mapLessonToLessonResponseDTO(lessonRepository.save(lesson));
    }

    public LessonResponseDTO findById(Long id) {
        return lessonMapper.mapLessonToLessonResponseDTO(findLessonById(id));
    }

    public Lesson findLessonById(Long id) {
        return lessonRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND, "Lesson", "ID", id))
        );
    }

    public void delete(Long id) {
        lessonRepository.delete(findLessonById(id));
    }

    public List<LessonResponseDTO> findAllByName(String name) {
        return lessonRepository.findAllByNameContainsIgnoreCase(name).stream()
                .map(lessonMapper::mapLessonToLessonResponseDTO)
                .toList();
    }

    public Page<LessonResponseDTO> findAllLessons(int page, int size, String sortBy, Sort.Direction order) {
        Pageable pageable = PageRequest.of(page - 1, size, order , sortBy);

        return lessonRepository.findAll(pageable).map(lessonMapper::mapLessonToLessonResponseDTO);
    }

    public List<LessonResponseDTO> findAllById(Set<Long> lessonIds) {
        return findAllLessonsWithIds(lessonIds).stream().map(lessonMapper::mapLessonToLessonResponseDTO).toList();
    }

    public LessonResponseDTO updateLesson(Long id, LessonUpdateRequestDTO dto) {
        Lesson foundLesson = findLessonById(id);
        User foundUser = userService.findUserById(dto.getTeacherId());

        userChecker.checkRole(foundUser, Role.TEACHER);

        foundLesson.setName(dto.getName());
        foundLesson.setCreditScore(dto.getCreditScore());
        foundLesson.setTeacher(foundUser);
        foundLesson.setIsMandatory(dto.getIsMandatory());

        return lessonMapper.mapLessonToLessonResponseDTO(lessonRepository.save(foundLesson));
    }
}
