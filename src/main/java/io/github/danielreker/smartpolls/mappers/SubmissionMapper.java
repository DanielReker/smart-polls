package io.github.danielreker.smartpolls.mappers;

import io.github.danielreker.smartpolls.dao.entities.SubmissionEntity;
import io.github.danielreker.smartpolls.web.dtos.SubmissionResponse;
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

    @Mapping(target = "pollId", source = "poll.id")
    SubmissionResponse toResponse(SubmissionEntity entity);

}
