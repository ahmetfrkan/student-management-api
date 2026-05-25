package com.tpe.student_management.service.mapper;

import com.tpe.student_management.dto.request.LessonCreateRequestDTO;
import com.tpe.student_management.dto.response.LessonResponseDTO;
import com.tpe.student_management.entity.logic.Lesson;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {
    private final UserMapper userMapper;

    public LessonMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Lesson mapLessonCreateRequestDTOToLesson(LessonCreateRequestDTO dto) {
        return Lesson.builder()
                .name(dto.getName())
                .creditScore(dto.getCreditScore())
                .isMandatory(dto.getIsMandatory())
                .build();
    }

    public LessonResponseDTO mapLessonToLessonResponseDTO(Lesson lesson) {
        return LessonResponseDTO.builder()
                .id(lesson.getId())
                .name(lesson.getName())
                .creditScore(lesson.getCreditScore())
                .isMandatory(lesson.getIsMandatory())
                .term(lesson.getTerm())
                .teacher(userMapper.mapUserToUserResponseDTO(lesson.getTeacher()))
                .build();
    }
}
