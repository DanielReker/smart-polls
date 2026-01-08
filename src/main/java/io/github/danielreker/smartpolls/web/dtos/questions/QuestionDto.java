package io.github.danielreker.smartpolls.web.dtos.questions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.danielreker.smartpolls.model.QuestionType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "dtype"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextQuestionDto.class, name = QuestionType.Names.TEXT),
        @JsonSubTypes.Type(value = SingleChoiceQuestionDto.class, name = QuestionType.Names.SINGLE_CHOICE),
        @JsonSubTypes.Type(value = MultiChoiceQuestionDto.class, name = QuestionType.Names.MULTI_CHOICE),
})
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public abstract class QuestionDto {

    @Nullable
    private final Long id;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private final String name;

    @Nullable
    private final String description;

    @NotNull
    private final Boolean isRequired;

    @NotNull
    @Min(0)
    private final Integer position;


    @JsonIgnore
    public abstract QuestionType getQuestionType();

}
