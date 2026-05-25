package com.tpe.student_management.contact_us.service;

import com.tpe.student_management.contact_us.dto.MessageRequestDTO;
import com.tpe.student_management.contact_us.dto.MessageResponseDTO;
import com.tpe.student_management.contact_us.entity.Message;
import com.tpe.student_management.contact_us.repository.MessageRepository;
import com.tpe.student_management.exception.ConflictException;
import com.tpe.student_management.messages.ErrorMessages;
import com.tpe.student_management.messages.SuccessMessages;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public Map<String,?> saveContactMessage(MessageRequestDTO dto) {
        Message message = messageMapper.mapMessageRequestDTOToMessage(dto);

        Message savedMessage = messageRepository.save(message);

        MessageResponseDTO responseDTO = messageMapper.mapMessageToMessageResponseDTO(savedMessage);

        return Map.of("info", "success", "message", responseDTO);
    }

    public Page<MessageResponseDTO> findAllWithPagination(int page, int size, String sortBy, Sort.Direction order) {
        //! Its purpose is to create different Pageable objects based on the order parameter.
        //! However, Jackson can already convert String values from JSON to Enum directly.

        Pageable pageable = PageRequest.of(page - 1, size, order, sortBy);

        return messageRepository.findAll(pageable).map(messageMapper::mapMessageToMessageResponseDTO);
    }

    public Page<MessageResponseDTO> findAllByEmail(int page, int size, String sortBy, Sort.Direction order, String email) {
        Pageable pageable = PageRequest.of(page - 1, size, order, sortBy);

        return messageRepository.findAllByEmail(email, pageable).map(messageMapper::mapMessageToMessageResponseDTO);
    }

    public Page<MessageResponseDTO> searchBySubject(String subject, int page, int size, String sort,
                                                           String type) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        if (Objects.equals(type, "desc")){
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        return messageRepository.findAllBySubject(subject, pageable). // Derived
                map(messageMapper::mapMessageToMessageResponseDTO);
    }

    public List<Message> searchByDateBetween(String beginDateString, String endDateString) {
        try {
            LocalDateTime beginDate = LocalDateTime.parse(beginDateString);
            LocalDateTime endDate = LocalDateTime.parse(endDateString);


            return messageRepository.findAllByCreatedAtBetween(beginDate, endDate);
        } catch (DateTimeParseException e) {
            throw new ConflictException(ErrorMessages.WRONG_DATE_FORMAT);
        }
    }

    public String deleteById(Long id) {
        findById(id);
        messageRepository.deleteById(id);
        return SuccessMessages.CONTACT_US_MESSAGE_DELETED;
    }

    public Message findById(Long id) {
        return messageRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(ErrorMessages.CONTACT_MESSAGE_ID_NOT_FOUND, id)));
    }
}
