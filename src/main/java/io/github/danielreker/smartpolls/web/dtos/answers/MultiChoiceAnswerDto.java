package io.github.danielreker.smartpolls.web.dtos.answers;

import io.github.danielreker.smartpolls.model.QuestionType;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
@Jacksonized
@EqualsAndHashCode(callSuper = true)
@ToString
public class MultiChoiceAnswerDto extends AnswerDto {

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTI_CHOICE;
    }

    @NotNull
    private List<Long> selectedChoiceIds;

}
