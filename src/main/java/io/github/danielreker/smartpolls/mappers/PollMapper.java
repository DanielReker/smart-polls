package io.github.danielreker.smartpolls.mappers;

import io.github.danielreker.smartpolls.dao.entities.PollEntity;
import io.github.danielreker.smartpolls.web.dtos.PollCreateRequest;
import io.github.danielreker.smartpolls.web.dtos.PollResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {QuestionMapper.class}
)
public interface PollMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "submissions", ignore = true)
    PollEntity createRequestToEntity(PollCreateRequest request);


    PollResponse toResponse(PollEntity entity);

}
