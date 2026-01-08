package io.github.danielreker.smartpolls.services;

import io.github.danielreker.smartpolls.dao.entities.answers.AnswerEntity;
import io.github.danielreker.smartpolls.dao.entities.questions.QuestionEntity;
import io.github.danielreker.smartpolls.model.QuestionType;
import io.github.danielreker.smartpolls.services.question.builders.QuestionBuilderService;
import io.github.danielreker.smartpolls.web.dtos.answers.AnswerDto;
import io.github.danielreker.smartpolls.web.dtos.questions.QuestionDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QuestionBuilderManagerService {

    private final Map<QuestionType, QuestionBuilderService<?, ?, ?>> questionBuilderServices;

    public QuestionBuilderManagerService(List<QuestionBuilderService<?, ?, ?>> questionBuilderServices) {
        this.questionBuilderServices = questionBuilderServices
                .stream()
                .collect(Collectors.toMap(
                        QuestionBuilderService::getQuestionType,
                        Function.identity(),
                        (questionBuilderService, _) -> {
                            throw new IllegalStateException("Found duplicated QuestionBuilderService for type: " +
                                    questionBuilderService.getQuestionType());
                        }
                ));

        for (QuestionType questionType : QuestionType.values()) {
            if (!this.questionBuilderServices.containsKey(questionType)) {
                throw new IllegalArgumentException("Couldn't find QuestionBuilderService for type: " + questionType);
            }
        }

    }

    // TODO: Fix/suppress unsafe cast
    public AnswerEntity buildAnswer(QuestionEntity question, AnswerDto answerDto) {
        final QuestionBuilderService<QuestionEntity, ?, ?> questionBuilderService =
                (QuestionBuilderService<QuestionEntity, ?, ?>) this.questionBuilderServices.get(question.getQuestionType());

        return questionBuilderService.buildAnswer(question, answerDto);
    }

    // TODO: Fix/suppress unsafe cast
    public QuestionEntity buildQuestion(QuestionDto questionDto) {
        final QuestionBuilderService<?, QuestionDto, ?> questionBuilderService =
                (QuestionBuilderService<?, QuestionDto, ?>) this.questionBuilderServices.get(questionDto.getQuestionType());

        return questionBuilderService.buildQuestion(questionDto);
    }



}
