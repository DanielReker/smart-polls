package io.github.danielreker.smartpolls.dao.entities.questions;

import io.github.danielreker.smartpolls.model.QuestionType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue(QuestionType.Names.TEXT)
public class TextQuestionEntity extends QuestionEntity {

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TEXT;
    }

    @Column(name = "max_length", columnDefinition = "int4")
    private Integer maxLength;

}