package io.github.danielreker.smartquizzes.dao.entities.questions;

import io.github.danielreker.smartquizzes.model.QuestionType;
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
public class TextQuestionEntity extends QuestionEntity {

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TEXT;
    }

    @Column(name = "max_length", columnDefinition = "int4")
    private Integer maxLength;

    @Column(name = "sample_correct_answer", columnDefinition = "text", length = Length.LONG32)
    private String sampleCorrectAnswer;

}