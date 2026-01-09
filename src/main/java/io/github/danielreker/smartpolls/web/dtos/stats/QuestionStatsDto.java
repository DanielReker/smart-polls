package io.github.danielreker.smartpolls.web.dtos.stats;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.danielreker.smartpolls.model.QuestionType;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "dtype"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextQuestionStatsDto.class, name = QuestionType.Names.TEXT),
        @JsonSubTypes.Type(value = SingleChoiceQuestionStatsDto.class, name = QuestionType.Names.SINGLE_CHOICE),
        @JsonSubTypes.Type(value = MultiChoiceQuestionStatsDto.class, name = QuestionType.Names.MULTI_CHOICE),
})
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public abstract class QuestionStatsDto {

    @NotNull
    private final Long questionId;

    @NotNull
    private final Long answerCount;


    @JsonIgnore
    public abstract QuestionType getQuestionType();

}
