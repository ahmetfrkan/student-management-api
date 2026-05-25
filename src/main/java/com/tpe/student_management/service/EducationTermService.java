package com.tpe.student_management.service;

import com.tpe.student_management.dto.request.EducationTermRequestDTO;
import com.tpe.student_management.dto.response.EducationTermResponseDTO;
import com.tpe.student_management.entity.logic.EducationTerm;
import com.tpe.student_management.exception.BadRequestException;
import com.tpe.student_management.exception.ConflictException;
import com.tpe.student_management.messages.ErrorMessages;
import com.tpe.student_management.repository.EducationTermRepository;
import com.tpe.student_management.service.mapper.EducationTermMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationTermService {
    private final EducationTermRepository educationTermRepository;
    private final EducationTermMapper educationTermMapper;

    public EducationTermResponseDTO findById(Long id) {
        return educationTermMapper.mapEducationTermToEducationTermResponseDTO(findEducationTermById(id));
    }

    public EducationTerm findEducationTermById(Long id) {
        return educationTermRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ErrorMessages.ENTITY_NOT_FOUND, "Education Term", "ID", id))
        );
    }

    public List<EducationTermResponseDTO> findAllEducationTerms() {
        return educationTermRepository.findAll()
                .stream()
                .map(educationTermMapper::mapEducationTermToEducationTermResponseDTO)
                .toList();
    }

    public void delete(Long id) {
        educationTermRepository.delete(findEducationTermById(id));
    }

    public EducationTermResponseDTO saveEducationTerm(EducationTermRequestDTO educationTermRequest) {
        validateEducationTermDates(educationTermRequest);
        EducationTerm savedEducationTerm =
                educationTermRepository.save(educationTermMapper.mapEducationTermRequestDTOToEducationTerm(educationTermRequest));

        return educationTermMapper.mapEducationTermToEducationTermResponseDTO(savedEducationTerm);
    }

    private void validateEducationTermDatesForRequest(EducationTermRequestDTO educationTermRequest) {
        // registrationDate cannot be after startDate
        if (educationTermRequest.getLastRegistrationDate().isAfter(educationTermRequest.getStartDate())) {
            throw new BadRequestException(ErrorMessages.TERM_START_EARLIER_THAN_LAST_REGISTRATION);
        }

        // endDate cannot be before startDate
        if (educationTermRequest.getEndDate().isBefore(educationTermRequest.getStartDate())) {
            throw new BadRequestException(ErrorMessages.TERM_END_EARLIER_THAN_START);
        }
    }

    private void validateEducationTermDates(EducationTermRequestDTO educationTermRequest) {
        // 1. Are the dates internally consistent?
        validateEducationTermDatesForRequest(educationTermRequest);

        // 2. Does the same term (Fall, Summer, etc.) already exist for this year?
        if (educationTermRepository.existsByTermAndStartDate_Year(
                educationTermRequest.getTerm(), educationTermRequest.getStartDate().getYear())) {
            throw new ConflictException(ErrorMessages.EDUCATION_TERM_ALREADY_EXISTS);
        }

        // 3. Does the new term overlap with any existing terms?
        boolean isOverlap = educationTermRepository.findByStartDate_YearOrEndDate_Year(educationTermRequest.getStartDate().getYear(), educationTermRequest.getEndDate().getYear())
                .stream()
                .anyMatch(educationTerm ->
                        !educationTerm.getStartDate().isAfter(educationTermRequest.getEndDate()) &&
                                !educationTerm.getEndDate().isBefore(educationTermRequest.getStartDate())
                );

        if (isOverlap) {
            throw new BadRequestException(ErrorMessages.TERM_CONFLICTS);
        }
    }

    public EducationTermResponseDTO updateEducationTerm(Long id, EducationTermRequestDTO dto) {
        EducationTerm existingEducationTerm = findEducationTermById(id);

        //? What if we want to update without changing the dates?
        if (!(dto.getLastRegistrationDate().equals(existingEducationTerm.getLastRegistrationDate()) &&
                dto.getStartDate().equals(existingEducationTerm.getStartDate()) &&
                dto.getEndDate().equals(existingEducationTerm.getEndDate()))) {
            validateEducationTermDates(dto);
        }

        existingEducationTerm.setTerm(dto.getTerm());
        existingEducationTerm.setLastRegistrationDate(dto.getLastRegistrationDate());
        existingEducationTerm.setStartDate(dto.getStartDate());
        existingEducationTerm.setEndDate(dto.getEndDate());

        EducationTerm updatedEducatedTerm = educationTermRepository.save(existingEducationTerm);

        return educationTermMapper.mapEducationTermToEducationTermResponseDTO(updatedEducatedTerm);
    }
}
