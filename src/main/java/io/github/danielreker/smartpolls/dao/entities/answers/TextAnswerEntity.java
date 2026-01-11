package io.github.danielreker.smartpolls.dao.entities.answers;

import io.github.danielreker.smartpolls.model.QuestionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Length;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TextAnswerTagEntity> tags = new ArrayList<>();

}