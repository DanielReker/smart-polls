package io.github.danielreker.smartquizzes.services;

import io.github.danielreker.smartquizzes.dao.entities.answers.AnswerEntity;
import io.github.danielreker.smartquizzes.dao.entities.questions.QuestionEntity;
import io.github.danielreker.smartquizzes.model.QuestionType;
import io.github.danielreker.smartquizzes.services.question.QuestionService;
import io.github.danielreker.smartquizzes.web.dtos.answers.AnswerDto;
import io.github.danielreker.smartquizzes.web.dtos.questions.QuestionDto;
import io.github.danielreker.smartquizzes.web.dtos.stats.QuestionStatsDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QuestionManagerService {

    private final Map<QuestionType, QuestionService<?, ?, ?>> questionServices;

    public QuestionManagerService(List<QuestionService<?, ?, ?>> questionServices) {
        this.questionServices = questionServices
                .stream()
                .collect(Collectors.toMap(
                        QuestionService::getQuestionType,
                        Function.identity(),
                        (questionService, _) -> {
                            throw new IllegalStateException("Found duplicated QuestionService for type: " +
                                    questionService.getQuestionType());
                        }
                ));

        for (QuestionType questionType : QuestionType.values()) {
            if (!this.questionServices.containsKey(questionType)) {
                throw new IllegalArgumentException("Couldn't find QuestionService for type: " + questionType);
            }
        }

    }

    public AnswerEntity buildAnswer(QuestionEntity question, AnswerDto answerDto) {
        final QuestionService<QuestionEntity, QuestionDto, AnswerDto> questionService =
                getQuestionService(question.getQuestionType());

        return questionService.buildAnswer(question, answerDto);
    }

    public QuestionEntity buildQuestion(QuestionDto questionDto) {
        final QuestionService<QuestionEntity, QuestionDto, AnswerDto> questionService =
                getQuestionService(questionDto.getQuestionType());

        return questionService.buildQuestion(questionDto);
    }

    public QuestionStatsDto getQuestionStats(QuestionEntity question) {
        final QuestionService<QuestionEntity, QuestionDto, AnswerDto> questionService =
                getQuestionService(question.getQuestionType());

        return questionService.getQuestionStats(question);
    }


    // Safe if service type is overridden correctly
    @SuppressWarnings("unchecked")
    private QuestionService<QuestionEntity, QuestionDto, AnswerDto> getQuestionService(QuestionType type) {
        return (QuestionService<QuestionEntity, QuestionDto, AnswerDto>) this.questionServices.get(type);
    }

}
