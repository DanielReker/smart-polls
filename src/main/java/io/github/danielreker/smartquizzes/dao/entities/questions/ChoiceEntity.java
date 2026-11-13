package io.github.danielreker.smartquizzes.dao.entities.questions;

import io.github.danielreker.smartquizzes.dao.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "choice",
        indexes = @Index(name = "idx_choice_question_id", columnList = "question_id")
)
public class ChoiceEntity extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity question;

    @Column(name = "name", columnDefinition = "varchar(255)", nullable = false)
    private String name;

    @Column(name = "position", columnDefinition = "int4", nullable = false)
    private Integer position;

    @Column(name = "is_correct", columnDefinition = "boolean", nullable = false)
    private Boolean isCorrect = false;

}