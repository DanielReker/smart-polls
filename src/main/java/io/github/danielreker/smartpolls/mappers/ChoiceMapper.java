package io.github.danielreker.smartpolls.mappers;

import io.github.danielreker.smartpolls.dao.entities.questions.ChoiceEntity;
import io.github.danielreker.smartpolls.web.dtos.questions.ChoiceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ChoiceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", ignore = true)
    ChoiceEntity toEntity(ChoiceDto dto);

}
