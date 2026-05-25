package com.tpe.student_management.controller;

import com.tpe.student_management.dto.request.UserCreateRequestDTO;
import com.tpe.student_management.dto.request.UserUpdateRequestDTO;
import com.tpe.student_management.dto.response.LoginResponseDTO;
import com.tpe.student_management.dto.response.UserResponseDTO;
import com.tpe.student_management.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/save/{role}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, ?>> saveUser(@PathVariable String role,
                                                   @RequestBody @Valid UserCreateRequestDTO dto) {
        return new ResponseEntity<>(userService.createUser(dto, role), HttpStatus.CREATED);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> findAllByRoleWithPagination(@RequestParam(required = false, defaultValue = "ADMIN") String role,
                                                                             @RequestParam(defaultValue = "1") int page,
                                                                             @RequestParam(required = false, defaultValue = "25") int size,
                                                                             @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                                             @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order) {
        return ResponseEntity.ok(userService.findAllByRole(role, page, size, sortBy, order));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id) {

        return ResponseEntity.ok(userService.findById(id));
    }


    //* 1 - deleteUserById() - ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    //* 2 - updateManagerOrAssistantManager() - ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Map<String, ?>> updateManagerOrAssistantManager(@PathVariable Long id, @RequestBody @Valid UserUpdateRequestDTO dto) {
        return ResponseEntity.ok(userService.updateManagerOrAssistantManager(id, dto));
    }

    //* 3 - updateUserOwnInfo() - All
    @PutMapping("/update-self")
    @PreAuthorize("permitAll()") // If roles that should NOT perform this action are identified replace permitAll() with hasAnyAuthority(...)
    public ResponseEntity<Map<String, ?>> updateUserOwnInfo(HttpServletRequest request, @RequestBody @Valid UserUpdateRequestDTO dto) {
        return ResponseEntity.ok(userService.updateUserOwnInfo(request, dto));
    }
    //* 4 - getUsersByName() - ADMIN, MANAGER, ASSISTANT_MANAGER
    @GetMapping("/search-by-name")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ASSISTANT_MANAGER')")
    public ResponseEntity<List<UserResponseDTO>> getUserByName(@RequestParam String firstName, @RequestParam String lastName) {
         return ResponseEntity.ok(userService.findUsersByName(firstName, lastName));
    }


}
