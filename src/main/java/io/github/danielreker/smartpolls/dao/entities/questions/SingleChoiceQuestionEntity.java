package io.github.danielreker.smartpolls.dao.entities.questions;

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
@DiscriminatorValue(QuestionType.Names.SINGLE_CHOICE)
public class SingleChoiceQuestionEntity extends QuestionEntity {

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.SINGLE_CHOICE;
    }

    @OneToMany(mappedBy = "question", orphanRemoval = true, cascade = CascadeType.ALL)
    @OrderBy("position ASC")
    private List<ChoiceEntity> possibleChoices = new ArrayList<>();

}