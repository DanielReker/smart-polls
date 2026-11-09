package io.github.danielreker.smartpolls.web.dtos.answers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.danielreker.smartpolls.model.QuestionType;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
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
        @JsonSubTypes.Type(value = TextAnswerDto.class, name = QuestionType.Names.TEXT),
        @JsonSubTypes.Type(value = SingleChoiceAnswerDto.class, name = QuestionType.Names.SINGLE_CHOICE),
        @JsonSubTypes.Type(value = MultiChoiceAnswerDto.class, name = QuestionType.Names.MULTI_CHOICE),
})
@Schema(
        discriminatorMapping = {
                @DiscriminatorMapping(value = QuestionType.Names.TEXT, schema = TextAnswerDto.class),
                @DiscriminatorMapping(value = QuestionType.Names.SINGLE_CHOICE, schema = SingleChoiceAnswerDto.class),
                @DiscriminatorMapping(value = QuestionType.Names.MULTI_CHOICE, schema = MultiChoiceAnswerDto.class),
        }
)
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public abstract class AnswerDto {

    @Nullable
    private final Long id;

    @NotNull
    private final Long questionId;


    @JsonIgnore
    public abstract QuestionType getQuestionType();

}
