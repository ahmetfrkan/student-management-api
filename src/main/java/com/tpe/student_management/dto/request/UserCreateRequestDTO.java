package com.tpe.student_management.dto.request;

import com.tpe.student_management.dto.request.abstracts.BaseUserRequestDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserCreateRequestDTO extends BaseUserRequestDTO {
    private Boolean isBuiltIn;
}
