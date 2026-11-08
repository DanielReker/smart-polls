package io.github.danielreker.smartpolls.services.question;

import io.github.danielreker.smartpolls.web.dtos.questions.QuestionDto;
import io.github.danielreker.smartpolls.web.dtos.questions.QuestionListDto;
import lombok.SneakyThrows;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Objects;

@Service
public class AiQuestionGeneratorService {

    private static final String SYSTEM_PROMPT = """
            Тебе предоставлен список текущих вопросов опроса и промпт (запрос пользователя).
            Ты должен обновить эти вопросы (или сгенерировать их с нуля, если в данный момент вопросов нет), то есть изменить старые или сгенерировать новые на основе пользовательского промпта.
            Выведи полный список обновленных вопросов в формате JSON в том же виде, что и на входе (но тебе следует оставить ID новых вопросов как null, потому что они в любом случае будут созданы заново, а твои ID будут проигнорированы).
            Существует несколько типов вопросов: "text", "single-choice", "multi-choice".
            Тип вопроса указывается в JSON-поле "dtype".
            
            Вот JSON-примеры типов вопросов с объяснениями:

            "single-choice" - вопрос с заранее заданными вариантами, где пользователя просят выбрать один из них (вопрос с одиночным выбором).
                {
                "id": null,
                "name": "Название вопроса с одиночным выбором",
                "description": "Более подробное описание вопроса с одиночным выбором, если необходимо", // может быть null, не стоит злоупотреблять, заполняй только в том случае, если без развёрнутого описания действительно не понятна суть вопроса
                "isRequired": true, // является ли ответ на этот вопрос обязательным для респондента
                "position": 0, // позиция вопроса в опросе, используется для сортировки на UI
                "dtype": "single-choice",
                "possibleChoices": [
                    {
                        "id": null,
                        "name": "Вариант 1",
                        "position": 0 // позиция варианта ответа в вопросе, используется для сортировки на UI
                    },
                    {
                        "id": null,
                        "name": "Вариант 2",
                        "position": 1
                    }
                ]
            }

            "multi-choice" - вопрос с заранее заданными вариантами, где пользователя просят выбрать несколько из них (вопрос с множественным выбором).
            {
                "id": null,
                "name": "Название вопроса с множественным выбором",
                "description": "Более детальное описание вопроса с множественным выбором, если необходимо", // может быть null, не стоит злоупотреблять, заполняй только в том случае, если без развёрнутого описания действительно не понятна суть вопроса
                "isRequired": true, // является ли ответ на этот вопрос обязательным для респондента
                "position": 0, // позиция вопроса в опросе, используется для сортировки в UI
                "dtype": "multi-choice",
                "possibleChoices": [
                    {
                        "id": null,
                        "name": "Вариант 1",
                        "position": 0 // позиция варианта ответа в вопросе, используется для сортировки в UI
                    },
                    {
                        "id": null,
                        "name": "Вариант 2",
                        "position": 1
                    }
                ]
            }

            "text" - вопрос с открытым текстовым полем, используется для запроса у пользователей информации в свободной форме (текстовый вопрос).
            {
                "id": null,
                "name": "Название текстового вопроса",
                "description": "Более детальное описание текстового вопроса, если необходимо", // может быть null, не стоит злоупотреблять, заполняй только в том случае, если без развёрнутого описания действительно не понятна суть вопроса
                "isRequired": true, // является ли ответ на этот вопрос обязательным для респондента
                "position": 0, // позиция вопроса в опросе, используется для сортировки на UI
                "dtype": "text",
                "maxLength": 200 // максимальная длина ответа, может быть null (если длина ответа не ограничена)
            }

            Обрати внимание, что JSON-тела с разными `dtype` полиморфны и содержат немного отличающиеся поля в зависимости от типа вопроса — это абсолютно нормально, так как вопросы разных `dtype` содержат разные данные.
            Генерируй вопросы, используя разные `dtype`: "text", "single-choice", "multi-choice"; не злоупотребляй каким-то одним из них.
            """;

    private final ChatClient chatClient;

    private final JsonMapper jsonMapper;

    public AiQuestionGeneratorService(ChatClient.Builder chatClientBuilder, JsonMapper jsonMapper) {
        this.chatClient = chatClientBuilder
                .defaultSystem(SYSTEM_PROMPT)
                .build();
        this.jsonMapper = jsonMapper;
    }


    @SneakyThrows
    public List<QuestionDto> transformQuestions(
            List<QuestionDto> oldQuestions,
            String prompt
    ) {
        final QuestionListDto oldQuestionList = new QuestionListDto(oldQuestions);

        final QuestionListDto result = chatClient
                .prompt(Prompt
                        .builder()
                        .messages(List.of(
                                UserMessage
                                        .builder()
                                        .text("Current questions: " + jsonMapper.writeValueAsString(oldQuestionList))
                                        .build(),
                                UserMessage
                                        .builder()
                                        .text(prompt)
                                        .build()
                        ))
                        .build())
                .advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
                .call()
                .entity(QuestionListDto.class);
        return Objects.requireNonNull(result).questions();
    }

}
