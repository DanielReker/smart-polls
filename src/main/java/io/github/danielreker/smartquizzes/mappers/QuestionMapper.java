package io.github.danielreker.smartquizzes.mappers;

import io.github.danielreker.smartquizzes.dao.entities.questions.MultiChoiceQuestionEntity;
import io.github.danielreker.smartquizzes.dao.entities.questions.QuestionEntity;
import io.github.danielreker.smartquizzes.dao.entities.questions.SingleChoiceQuestionEntity;
import io.github.danielreker.smartquizzes.dao.entities.questions.TextQuestionEntity;
import io.github.danielreker.smartquizzes.web.dtos.questions.MultiChoiceQuestionDto;
import io.github.danielreker.smartquizzes.web.dtos.questions.QuestionDto;
import io.github.danielreker.smartquizzes.web.dtos.questions.SingleChoiceQuestionDto;
import io.github.danielreker.smartquizzes.web.dtos.questions.TextQuestionDto;
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
    @Mapping(target = "quiz", ignore = true)
    @Mapping(target = "hints", ignore = true)
    TextQuestionEntity toEntity(TextQuestionDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quiz", ignore = true)
    @Mapping(target = "possibleChoices", ignore = true)
    @Mapping(target = "hints", ignore = true)
    SingleChoiceQuestionEntity toEntity(SingleChoiceQuestionDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quiz", ignore = true)
    @Mapping(target = "possibleChoices", ignore = true)
    @Mapping(target = "hints", ignore = true)
    MultiChoiceQuestionEntity toEntity(MultiChoiceQuestionDto dto);

}
