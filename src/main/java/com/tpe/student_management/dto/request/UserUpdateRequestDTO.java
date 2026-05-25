package com.tpe.student_management.dto.request;

import com.tpe.student_management.dto.request.abstracts.AbstractUserRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UserUpdateRequestDTO extends AbstractUserRequestDTO {
}
