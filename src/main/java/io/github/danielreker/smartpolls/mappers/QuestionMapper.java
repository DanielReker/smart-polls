package io.github.danielreker.smartpolls.mappers;

import io.github.danielreker.smartpolls.dao.entities.questions.MultiChoiceQuestionEntity;
import io.github.danielreker.smartpolls.dao.entities.questions.QuestionEntity;
import io.github.danielreker.smartpolls.dao.entities.questions.SingleChoiceQuestionEntity;
import io.github.danielreker.smartpolls.dao.entities.questions.TextQuestionEntity;
import io.github.danielreker.smartpolls.web.dtos.questions.MultiChoiceQuestionDto;
import io.github.danielreker.smartpolls.web.dtos.questions.QuestionDto;
import io.github.danielreker.smartpolls.web.dtos.questions.SingleChoiceQuestionDto;
import io.github.danielreker.smartpolls.web.dtos.questions.TextQuestionDto;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION
)
public interface QuestionMapper {

    @SubclassMapping(source = SingleChoiceQuestionEntity.class, target = SingleChoiceQuestionDto.class)
    @SubclassMapping(source = MultiChoiceQuestionEntity.class, target = MultiChoiceQuestionDto.class)
    @SubclassMapping(source = TextQuestionEntity.class, target = TextQuestionDto.class)
    QuestionDto toDto(QuestionEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "poll", ignore = true)
    TextQuestionEntity toEntity(TextQuestionDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "poll", ignore = true)
    @Mapping(target = "possibleChoices", ignore = true)
    SingleChoiceQuestionEntity toEntity(SingleChoiceQuestionDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "poll", ignore = true)
    @Mapping(target = "possibleChoices", ignore = true)
    MultiChoiceQuestionEntity toEntity(MultiChoiceQuestionDto dto);

}
