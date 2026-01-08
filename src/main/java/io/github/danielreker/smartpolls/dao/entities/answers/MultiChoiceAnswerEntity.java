package io.github.danielreker.smartpolls.dao.entities.answers;

import io.github.danielreker.smartpolls.dao.entities.questions.ChoiceEntity;
import io.github.danielreker.smartpolls.model.QuestionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue(QuestionType.Names.MULTI_CHOICE)
public class MultiChoiceAnswerEntity extends AnswerEntity {

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTI_CHOICE;
    }


    @ManyToMany
    @JoinTable(
            name = "answer_choice",
            joinColumns = @JoinColumn(name = "answer_id"),
            inverseJoinColumns = @JoinColumn(name = "choice_id")
    )
    @OrderBy("position ASC")
    private List<ChoiceEntity> selectedChoices = new ArrayList<>();

}