package io.github.danielreker.smartquizzes.mappers;

import io.github.danielreker.smartquizzes.dao.entities.QuizEntity;
import io.github.danielreker.smartquizzes.web.dtos.QuizCreateRequest;
import io.github.danielreker.smartquizzes.web.dtos.QuizResponse;
import io.github.danielreker.smartquizzes.web.dtos.ShortQuizResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {QuestionMapper.class}
)
public interface QuizMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "submissions", ignore = true)
    @Mapping(target = "owner", ignore = true)
    QuizEntity createRequestToEntity(QuizCreateRequest request);


    QuizResponse toResponse(QuizEntity entity, Long mySubmissionsCount);

    ShortQuizResponse toShortResponse(QuizEntity entity);

}
