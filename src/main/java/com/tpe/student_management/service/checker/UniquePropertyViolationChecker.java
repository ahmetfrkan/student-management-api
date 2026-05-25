package com.tpe.student_management.service.checker;

import com.tpe.student_management.dto.request.abstracts.AbstractUserRequestDTO;
import com.tpe.student_management.entity.user.User;
import com.tpe.student_management.exception.ConflictException;
import com.tpe.student_management.messages.ErrorMessages;
import com.tpe.student_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniquePropertyViolationChecker {
    //! DB access is required for this check, so a Repository must be injected.
    //! Therefore this class must be a @Component.
    private final UserRepository userRepository;

    public void checkProperties(String username, String email, String phoneNumber, String ssn) {
        if (userRepository.existsByUsername(username)) {
            throw new ConflictException(String.format(ErrorMessages.UNIQUE_PROPERTY_VIOLATION, "Username", username));
        }
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(String.format(ErrorMessages.UNIQUE_PROPERTY_VIOLATION, "Email", email));
        }
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new ConflictException(String.format(ErrorMessages.UNIQUE_PROPERTY_VIOLATION, "Phone number", phoneNumber));
        }
        if (userRepository.existsBySsn(ssn)) {
            throw new ConflictException(String.format(ErrorMessages.UNIQUE_PROPERTY_VIOLATION, "SSN", ssn));
        }
    }
    //! This method variant skips DB calls if no fields have actually changed.
    public void checkProperties(User user, AbstractUserRequestDTO abstractUserRequest){
        String updatedUsername = "";
        String updatedSnn = "";
        String updatedPhone = "";
        String updatedEmail = "";
        boolean isChanged = false;

        if(!user.getUsername().equalsIgnoreCase(abstractUserRequest.getUsername())){
            updatedUsername = abstractUserRequest.getUsername();
            isChanged = true;
        }
        if(!user.getSsn().equalsIgnoreCase(abstractUserRequest.getSsn())){
            updatedSnn = abstractUserRequest.getSsn();
            isChanged = true;
        }
        if(!user.getPhoneNumber().equalsIgnoreCase(abstractUserRequest.getPhoneNumber())){
            updatedPhone = abstractUserRequest.getPhoneNumber();
            isChanged = true;
        }
        if(!user.getEmail().equalsIgnoreCase(abstractUserRequest.getEmail())){
            updatedEmail = abstractUserRequest.getEmail();
            isChanged = true;
        }

        if(isChanged) {
            checkProperties(updatedUsername, updatedSnn, updatedPhone, updatedEmail);
        }

    }
}
