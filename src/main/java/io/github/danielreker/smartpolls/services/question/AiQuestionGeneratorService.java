package io.github.danielreker.smartpolls.services.question;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.danielreker.smartpolls.web.dtos.questions.QuestionDto;
import io.github.danielreker.smartpolls.web.dtos.questions.QuestionListDto;
import lombok.SneakyThrows;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AiQuestionGeneratorService {

    private static final String SYSTEM_PROMPT = """
            You are given a list of poll current questions and a prompt.
            You must update given questions (or generate them from scratch \
            if there's currently no questions), i.e. change old ones or \
            generate new, based on user's prompt.
            Output complete list of updated questions as JSON in same format as input \
            (but you should leave new question IDs null, because they will be recreated \
            anyway and your IDs will be ignored)
            There are multiple types of questions: "text", "single-choice", "multi-choice". \
            Question's type is encoded in JSON field "dtype".
            Here are JSON examples of question types with explanations:
            "single-choice" - question with pre-defined options that asks user \
            to select one of that options.
            {
              "id": null,
              "name": "Single choice question name",
              "description": "More detailed single choice question description if necessary", // nullable, shouldn't be overused
              "isRequired": true, // is answer to that question mandatory
              "position": 0, // question position in poll, used to sort questions on UI
              "dtype": "single-choice",
              "possibleChoices": [
                {
                  "id": null,
                  "name": "Option 1",
                  "position": 0 // option position in question, used to sort options on UI
                },
                {
                  "id": null,
                  "name": "Option 2",
                  "position": 1
                }
              ]
            }
            "multi-choice" - question with pre-defined options that asks user \
            to select multiple of that options.
            {
              "id": null,
              "name": "Multi choice question name",
              "description": "More detailed multi choice question description if necessary", // nullable, shouldn't be overused
              "isRequired": true, // is answer to that question mandatory
              "position": 0, // question position in poll, used to sort questions on UI
              "dtype": "multi-choice",
              "possibleChoices": [
                {
                  "id": null,
                  "name": "Option 1",
                  "position": 0 // option position in question, used to sort options on UI
                },
                {
                  "id": null,
                  "name": "Option 2",
                  "position": 1
                }
              ]
            }
            "text" - open-ended text field question, used for requesting from users information in free form
            {
              "id": null,
              "name": "Text question name",
              "description": "More detailed text question description if necessary", // nullable, shouldn't be overused
              "isRequired": true, // is answer to that question mandatory
              "position": 0, // question position in poll, used to sort questions on UI
              "dtype": "text",
              "maxLength": 200 // max length of answer, nullable
            }
            Note that question dtypes JSON bodies are polymorphic and contain slightly different fields depending on \
            question type - that's completely OK as  questions of different dtypes contain different data.
            Generate questions using different dtypes: "text", "single-choice", "multi-choice"; \
            don't overuse single one
            """;

    private final ChatClient chatClient;

    private final ObjectMapper objectMapper;

    public AiQuestionGeneratorService(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
        this.chatClient = chatClientBuilder
                .defaultSystem(SYSTEM_PROMPT)
                .build();
        this.objectMapper = objectMapper;
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
                                        .text("Current questions: " + objectMapper.writeValueAsString(oldQuestionList))
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
