package com.tpe.student_management.service;

import com.tpe.student_management.dto.request.UserCreateRequestDTO;
import com.tpe.student_management.dto.request.UserUpdateRequestDTO;
import com.tpe.student_management.dto.response.UserResponseDTO;
import com.tpe.student_management.entity.user.User;
import com.tpe.student_management.entity.user.UserRole;
import com.tpe.student_management.exception.BadRequestException;
import com.tpe.student_management.exception.UnallowedOperationException;
import com.tpe.student_management.repository.UserRepository;
import com.tpe.student_management.repository.UserRoleRepository;
import com.tpe.student_management.service.checker.UniquePropertyViolationChecker;
import com.tpe.student_management.service.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UniquePropertyViolationChecker uniquePropertyViolationChecker;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;
    private final UserRoleRepository userRoleRepository;

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException("No user found with given username: " + username)
        );
    }

    public Map<String, ?> createUser(UserCreateRequestDTO dto, String role) {
        //! role parameter must be one of: ADMIN, MANAGER, ASSISTANT_MANAGER. Any other value throws an exception.
        if (!(role.equals("ADMIN") || role.equals("MANAGER") || role.equals("ASSISTANT_MANAGER"))) {
            throw new UnallowedOperationException("This operation is only for creation of ADMIN, MANAGER, ASSISTANT_MANAGER users.");
        }

        //! 1 - Unique property validation
        uniquePropertyViolationChecker.checkProperties(dto.getUsername(), dto.getEmail(), dto.getPhoneNumber(), dto.getSsn());

        //! 2 - DTO -> Entity mapping
        User user = userMapper.mapUserCreateRequestDTOToUser(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUserRole(userRoleService.findUserRoleByRoleName(role));

        if (role.equals("ADMIN")) {
            //! isBuiltIn is only applied when the role is ADMIN; ignored for all other roles.
            user.setIsBuiltIn(dto.getIsBuiltIn());
        }

        //! 3 - Save to database
        User savedUser = userRepository.save(user);

        return Map.of("message", role + " created successfully.");
    }

    public Page<UserResponseDTO> findAllByRole(String role, int page, int size, String sortBy, Sort.Direction order) {
        UserRole userRole = userRoleService.findUserRoleByRoleName(role);

        Pageable pageable = PageRequest.of(page - 1, size, order, sortBy);

        return userRepository.findAllByUserRole(userRole, pageable)
                .map(userMapper::mapUserToUserResponseDTO);
    }

    public UserResponseDTO findById(Long id) {
        return userMapper.mapUserToUserResponseDTO(findUserById(id));
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No user found with given ID: " + id)
        );
    }

    public void deleteUser(Long id) {

        userRepository.delete(findUserById(id));
    }

    public Map<String,?> updateManagerOrAssistantManager(Long id, UserUpdateRequestDTO dto) {
        User user = findUserById(id);

        if (!(user.getUserRole().getRole().name().equals("MANAGER")
                || user.getUserRole().getRole().name().equals("ASSISTANT_MANAGER"))) {
            throw new BadRequestException("User is not a Manager or Assistant Manager.");
        }

        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setBirthDate(dto.getDateOfBirth());
        user.setSsn(dto.getSsn());
        user.setBirthPlace(dto.getBirthPlace());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setGender(dto.getGender());
        user.setEmail(dto.getEmail());

        User updatedUser = userRepository.save(user);

        return Map.of("message", "User updated successfully.",
                "newInformation", userMapper.mapUserToUserResponseDTO(updatedUser));
    }

    public Map<String,?> updateUserOwnInfo(HttpServletRequest request, UserUpdateRequestDTO dto) {
        User user = findUserByUsername((String) request.getAttribute("username"));

        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setBirthDate(dto.getDateOfBirth());
        user.setSsn(dto.getSsn());
        user.setBirthPlace(dto.getBirthPlace());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setGender(dto.getGender());
        user.setEmail(dto.getEmail());

        User updatedUser = userRepository.save(user);

        return Map.of("message", "User updated successfully.",
                "newInformation", userMapper.mapUserToUserResponseDTO(updatedUser));
    }

    public List<UserResponseDTO> findUsersByName(String firstName, String lastName) {

        return userRepository.findAllByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName)
                .stream().map(userMapper::mapUserToUserResponseDTO).toList();
    }
}
