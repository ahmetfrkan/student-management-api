package com.tpe.student_management.service.mapper;

import com.tpe.student_management.dto.request.StudentCreateRequestDTO;
import com.tpe.student_management.dto.request.TeacherCreateRequestDTO;
import com.tpe.student_management.dto.request.UserCreateRequestDTO;
import com.tpe.student_management.dto.response.UserResponseDTO;
import com.tpe.student_management.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    // Note: BaseUserRequestDTO could be used instead of UserCreateRequestDTO here.
    // This would allow Teacher and Student mappings to reuse the same method in the future.

    public User mapUserCreateRequestDTOToUser(UserCreateRequestDTO dto) {
        return User.builder()
                .username(dto.getUsername())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .ssn(dto.getSsn())
                .birthDate(dto.getDateOfBirth())
                .birthPlace(dto.getBirthPlace())
                .phoneNumber(dto.getPhoneNumber())
                .gender(dto.getGender())
                .build();
    }

    public UserResponseDTO mapUserToUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .ssn(user.getSsn())
                .birthDate(user.getBirthDate())
                .birthPlace(user.getBirthPlace())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .userRole(user.getUserRole())
                .isActive(user.getIsActive())
                .isAdvisor(user.getIsAdvisor())
                .isBuiltIn(user.getIsBuiltIn())
                .motherName(user.getMotherName())
                .fatherName(user.getFatherName())
                .studentNumber(user.getStudentNumber())
                .advisorTeacherId(user.getAdvisorTeacherId())
                .build();
    }


    public User mapTeacherCreateRequestDTOToUser(TeacherCreateRequestDTO dto){
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .ssn(dto.getSsn())
                .username(dto.getUsername())
                .birthDate(dto.getDateOfBirth())
                .birthPlace(dto.getBirthPlace())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .isAdvisor(dto.getIsAdvisor())
                .gender(dto.getGender())
                .build();
    }

    public User mapStudentCreateRequestDTOToUser(StudentCreateRequestDTO studentRequest) {
        return User.builder()
                .fatherName(studentRequest.getFatherName())
                .motherName(studentRequest.getMotherName())
                .birthDate(studentRequest.getDateOfBirth())
                .birthPlace(studentRequest.getBirthPlace())
                .firstName(studentRequest.getFirstName())
                .lastName(studentRequest.getLastName())
                .password(studentRequest.getPassword())
                .username(studentRequest.getUsername())
                .ssn(studentRequest.getSsn())
                .email(studentRequest.getEmail())
                .phoneNumber(studentRequest.getPhoneNumber())
                .gender(studentRequest.getGender())
                .build();
    }

    public User mapTeacherRequestToUpdatedUser(TeacherCreateRequestDTO userRequest, Long userId){
        return User.builder()
                .id(userId)
                .username(userRequest.getUsername())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .password(userRequest.getPassword())
                .ssn(userRequest.getSsn())
                .birthDate(userRequest.getDateOfBirth())
                .birthPlace(userRequest.getBirthPlace())
                .phoneNumber(userRequest.getPhoneNumber())
                .gender(userRequest.getGender())
                .email(userRequest.getEmail())
                .isAdvisor(userRequest.getIsAdvisor())
                .build();
    }
}
