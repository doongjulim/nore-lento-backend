package io.github.dongjulim.domain.user.dto;

import io.github.dongjulim.domain.user.enums.Grade;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UpdateUserGradeRequest {

    @NotNull
    private Grade grade;
}
