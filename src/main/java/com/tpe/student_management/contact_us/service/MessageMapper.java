package com.tpe.student_management.contact_us.service;

import com.tpe.student_management.contact_us.dto.MessageRequestDTO;
import com.tpe.student_management.contact_us.dto.MessageResponseDTO;
import com.tpe.student_management.contact_us.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {
    public Message mapMessageRequestDTOToMessage(MessageRequestDTO dto){
        return Message.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .subject(dto.getSubject())
                .message(dto.getMessage())
                .build();
    }
    
    public MessageResponseDTO mapMessageToMessageResponseDTO(Message message) {
        return MessageResponseDTO.builder()
                .id(message.getId())
                .firstName(message.getFirstName())
                .lastName(message.getLastName())
                .email(message.getEmail())
                .subject(message.getSubject())
                .message(message.getMessage())
                .build();
    }
}
