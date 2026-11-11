package io.github.danielreker.smartquizzes.mappers;

import io.github.danielreker.smartquizzes.dao.entities.answers.AnswerEntity;
import io.github.danielreker.smartquizzes.dao.entities.answers.MultiChoiceAnswerEntity;
import io.github.danielreker.smartquizzes.dao.entities.answers.SingleChoiceAnswerEntity;
import io.github.danielreker.smartquizzes.dao.entities.answers.TextAnswerEntity;
import io.github.danielreker.smartquizzes.dao.entities.questions.ChoiceEntity;
import io.github.danielreker.smartquizzes.web.dtos.answers.AnswerDto;
import io.github.danielreker.smartquizzes.web.dtos.answers.MultiChoiceAnswerDto;
import io.github.danielreker.smartquizzes.web.dtos.answers.SingleChoiceAnswerDto;
import io.github.danielreker.smartquizzes.web.dtos.answers.TextAnswerDto;
import org.mapstruct.*;

import java.util.Optional;
import java.util.UUID;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION
)
public interface AnswerMapper {

    @SubclassMapping(source = SingleChoiceAnswerEntity.class, target = SingleChoiceAnswerDto.class)
    @SubclassMapping(source = MultiChoiceAnswerEntity.class, target = MultiChoiceAnswerDto.class)
    @SubclassMapping(source = TextAnswerEntity.class, target = TextAnswerDto.class)
    AnswerDto toDto(AnswerEntity entity);

    @Mapping(target = "questionId", source = "question.id")
    TextAnswerDto toDto(TextAnswerEntity entity);

    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "selectedChoiceId", source = "selectedChoice.id")
    SingleChoiceAnswerDto toDto(SingleChoiceAnswerEntity entity);

    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "selectedChoiceIds", source = "selectedChoices")
    MultiChoiceAnswerDto toDto(MultiChoiceAnswerEntity entity);

    default UUID choiceEntityToId(ChoiceEntity choiceEntity) {
        return Optional
                .ofNullable(choiceEntity)
                .map(ChoiceEntity::getId)
                .orElse(null);
    }


}
