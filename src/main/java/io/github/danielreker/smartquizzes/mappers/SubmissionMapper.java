package io.github.danielreker.smartquizzes.mappers;

import io.github.danielreker.smartquizzes.dao.entities.SubmissionEntity;
import io.github.danielreker.smartquizzes.web.dtos.SubmissionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {AnswerMapper.class}
)
public interface SubmissionMapper {

    @Mapping(target = "quizId", source = "quiz.id")
    SubmissionResponse toResponse(SubmissionEntity entity);

}
