package io.github.danielreker.smartquizzes.dao.entities.questions;

import io.github.danielreker.smartquizzes.dao.entities.BaseEntity;
import io.github.danielreker.smartquizzes.dao.entities.QuizEntity;
import io.github.danielreker.smartquizzes.model.QuestionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Length;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(
        name = "question",
        indexes = @Index(name = "idx_question_quiz_id", columnList = "quiz_id")
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class QuestionEntity extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    private QuizEntity quiz;

    @Column(name = "name", columnDefinition = "varchar(255)", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "text", length = Length.LONG32)
    private String description;

    @Column(name = "position", columnDefinition = "int4", nullable = false)
    private Integer position;

    @Column(name = "is_required", columnDefinition = "boolean", nullable = false)
    private Boolean isRequired = false;

    @Column(name = "max_score", columnDefinition = "numeric(10,3)", nullable = false, precision = 10, scale = 3)
    private BigDecimal maxScore;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sequentialOrder ASC")
    private List<HintEntity> hints = new ArrayList<>();


    public abstract QuestionType getQuestionType();

}