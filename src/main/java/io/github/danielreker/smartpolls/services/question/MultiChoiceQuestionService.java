package io.github.danielreker.smartpolls.services.question;

import io.github.danielreker.smartpolls.dao.entities.answers.MultiChoiceAnswerEntity;
import io.github.danielreker.smartpolls.dao.entities.questions.ChoiceEntity;
import io.github.danielreker.smartpolls.dao.entities.questions.MultiChoiceQuestionEntity;
import io.github.danielreker.smartpolls.dao.repositories.answers.MultiChoiceAnswerRepository;
import io.github.danielreker.smartpolls.mappers.ChoiceMapper;
import io.github.danielreker.smartpolls.mappers.QuestionMapper;
import io.github.danielreker.smartpolls.model.QuestionType;
import io.github.danielreker.smartpolls.web.dtos.answers.MultiChoiceAnswerDto;
import io.github.danielreker.smartpolls.web.dtos.questions.MultiChoiceQuestionDto;
import io.github.danielreker.smartpolls.web.dtos.stats.ChoiceStatsDto;
import io.github.danielreker.smartpolls.web.dtos.stats.MultiChoiceQuestionStatsDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MultiChoiceQuestionService
        extends QuestionService<MultiChoiceQuestionEntity, MultiChoiceQuestionDto, MultiChoiceAnswerDto> {

    private final QuestionMapper questionMapper;

    private final ChoiceMapper choiceMapper;

    private final MultiChoiceAnswerRepository multiChoiceAnswerRepository;

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTI_CHOICE;
    }

    @Override
    public MultiChoiceAnswerEntity buildConcreteAnswer(MultiChoiceQuestionEntity question, MultiChoiceAnswerDto answerDto) {
        final List<ChoiceEntity> choices = answerDto
                .getSelectedChoiceIds()
                .stream()
                .map(selectedChoiceId -> question
                        .getPossibleChoices()
                        .stream()
                        .filter(item -> Objects.equals(item.getId(), selectedChoiceId))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Could not find choice with id " +
                                selectedChoiceId + " in question " + question.getId())))
                .toList();

        final MultiChoiceAnswerEntity answerEntity = new MultiChoiceAnswerEntity();
        answerEntity.setSelectedChoices(choices);
        answerEntity.setQuestion(question);
        return answerEntity;
    }

    @Override
    public MultiChoiceQuestionEntity buildQuestion(MultiChoiceQuestionDto questionDto) {
        final MultiChoiceQuestionEntity question = questionMapper.toEntity(questionDto);

        question.setPossibleChoices(questionDto
                .getPossibleChoices()
                .stream()
                .map(choiceMapper::toEntity)
                .peek(choice -> choice.setQuestion(question))
                .toList());

        return question;
    }

    @Override
    public MultiChoiceQuestionStatsDto getQuestionStats(MultiChoiceQuestionEntity question) {
        return MultiChoiceQuestionStatsDto
                .builder()
                .questionId(question.getId())
                .answerCount(multiChoiceAnswerRepository.countByQuestionId(question.getId()))
                .choiceStats(question
                        .getPossibleChoices()
                        .stream()
                        .map(choice -> ChoiceStatsDto
                                .builder()
                                .id(choice.getId())
                                .count(multiChoiceAnswerRepository.countChoiceSelectionsByChoiceId(choice.getId()))
                                .build())
                        .toList())
                .build();
    }

}
