package io.github.danielreker.smartquizzes.web.dtos.questions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.danielreker.smartquizzes.model.QuestionType;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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
@Schema(
        discriminatorMapping = {
                @DiscriminatorMapping(value = QuestionType.Names.TEXT, schema = TextQuestionDto.class),
                @DiscriminatorMapping(value = QuestionType.Names.SINGLE_CHOICE, schema = SingleChoiceQuestionDto.class),
                @DiscriminatorMapping(value = QuestionType.Names.MULTI_CHOICE, schema = MultiChoiceQuestionDto.class),
        }
)
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public abstract class QuestionDto {

    @Nullable
    private final UUID id;

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

    @NotNull
    @PositiveOrZero
    private final BigDecimal maxScore;

    @NotNull
    private final List<@NotNull @Valid HintDto> hints;


    @JsonIgnore
    public abstract QuestionType getQuestionType();

}
