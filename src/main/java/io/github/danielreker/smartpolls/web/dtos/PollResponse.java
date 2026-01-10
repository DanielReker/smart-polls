package io.github.danielreker.smartpolls.web.dtos;

import io.github.danielreker.smartpolls.dao.entities.PollStatus;
import io.github.danielreker.smartpolls.web.dtos.questions.QuestionDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class PollResponse {

    @NotNull
    Long id;

    @NotNull
    Instant createdDate;

    @NotNull
    @NotBlank
    @Size(max = 255)
    String name;

    @Nullable
    String description;

    @NotNull
    PollStatus status;

    @NotNull
    List<QuestionDto> questions;

    @NotNull
    Long mySubmissionsCount;

}
