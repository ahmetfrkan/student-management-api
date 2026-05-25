package com.tpe.student_management.service.mapper;

import com.tpe.student_management.dto.request.EducationTermRequestDTO;
import com.tpe.student_management.dto.response.EducationTermResponseDTO;
import com.tpe.student_management.entity.logic.EducationTerm;
import org.springframework.stereotype.Component;

@Component
public class EducationTermMapper {
    public EducationTermResponseDTO mapEducationTermToEducationTermResponseDTO(EducationTerm educationTerm) {
        return EducationTermResponseDTO.builder()
                .id(educationTerm.getId())
                .term(educationTerm.getTerm().name())
                .startDate(educationTerm.getStartDate())
                .endDate(educationTerm.getEndDate())
                .lastRegistrationDate(educationTerm.getLastRegistrationDate())
                .build();
    }

    public EducationTerm mapEducationTermRequestDTOToEducationTerm(EducationTermRequestDTO dto) {
        return EducationTerm.builder()
                .term(dto.getTerm())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .lastRegistrationDate(dto.getLastRegistrationDate())
                .build();
    }
}
