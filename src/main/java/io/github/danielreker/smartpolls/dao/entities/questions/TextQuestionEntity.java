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
@DiscriminatorValue(QuestionType.Names.TEXT)
public class TextQuestionEntity extends QuestionEntity {

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TEXT;
    }

    @Column(name = "max_length", columnDefinition = "int4")
    private Integer maxLength;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("count ASC")
    private List<TextQuestionSummaryTagEntity> tags = new ArrayList<>();

}