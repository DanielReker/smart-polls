package io.github.danielreker.smartquizzes.mappers;

import io.github.danielreker.smartquizzes.dao.entities.questions.HintEntity;
import io.github.danielreker.smartquizzes.web.dtos.questions.HintDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface HintMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", ignore = true)
    HintEntity toEntity(HintDto dto);

}
