package com.tpe.student_management.dto.request.abstracts;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BaseUserRequestDTO extends AbstractUserRequestDTO {
    @NotNull(message = "Please Enter your password")
    @Size(min = 8,max = 60,message = "Your password should be at least 8 chars or maximum 60 characters")
    private String password;
}
