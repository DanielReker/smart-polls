package io.github.danielreker.smartpolls.dao.entities.questions;

import io.github.danielreker.smartpolls.dao.entities.BaseEntity;
import io.github.danielreker.smartpolls.dao.entities.PollEntity;
import io.github.danielreker.smartpolls.model.QuestionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Length;

@Setter
@Getter
@Entity
@Table(
        name = "question",
        indexes = @Index(name = "idx_question_poll_id", columnList = "poll_id")
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class QuestionEntity extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "poll_id", nullable = false)
    private PollEntity poll;

    @Column(name = "name", columnDefinition = "varchar(255)", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "text", length = Length.LONG32)
    private String description;



    @Column(name = "position", columnDefinition = "int4", nullable = false)
    private Integer position;

    @Column(name = "is_required", columnDefinition = "boolean", nullable = false)
    private Boolean isRequired = false;

    public abstract QuestionType getQuestionType();

}