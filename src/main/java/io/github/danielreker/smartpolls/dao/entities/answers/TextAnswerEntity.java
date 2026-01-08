package io.github.danielreker.smartpolls.dao.entities.answers;

import io.github.danielreker.smartpolls.model.QuestionType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Length;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue(QuestionType.Names.TEXT)
public class TextAnswerEntity extends AnswerEntity {

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TEXT;
    }

    @Column(name = "value", columnDefinition = "text", length = Length.LONG32)
    private String value;

}