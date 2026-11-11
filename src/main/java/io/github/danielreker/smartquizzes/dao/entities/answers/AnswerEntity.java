package io.github.danielreker.smartquizzes.dao.entities.answers;

import io.github.danielreker.smartquizzes.dao.entities.BaseEntity;
import io.github.danielreker.smartquizzes.dao.entities.SubmissionEntity;
import io.github.danielreker.smartquizzes.dao.entities.questions.QuestionEntity;
import io.github.danielreker.smartquizzes.model.QuestionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "answer",
        indexes = @Index(name = "idx_answer_submission_id", columnList = "submission_id")
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class AnswerEntity extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "submission_id", nullable = false)
    private SubmissionEntity submission;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity question;


    public abstract QuestionType getQuestionType();

}