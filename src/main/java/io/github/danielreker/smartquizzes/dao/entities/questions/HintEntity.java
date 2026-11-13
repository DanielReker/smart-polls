package io.github.danielreker.smartquizzes.dao.entities.questions;

import io.github.danielreker.smartquizzes.dao.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Length;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "hint")
public class HintEntity extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity question;

    @Column(name = "sequential_order", columnDefinition = "int4", nullable = false)
    private Integer sequentialOrder;

    @Column(name = "score_penalty", columnDefinition = "numeric(10,3)", nullable = false, precision = 10, scale = 3)
    private BigDecimal scorePenalty;

    @Column(name = "text", columnDefinition = "text", length = Length.LONG32, nullable = false)
    private String text;

}