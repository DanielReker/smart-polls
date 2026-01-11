package io.github.danielreker.smartpolls.dao.entities.questions;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "text_question_summary_tag")
public class TextQuestionSummaryTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private TextQuestionEntity question;

    @Column(name = "name", columnDefinition = "varchar(255)", nullable = false)
    private String name;

    @Column(name = "count", columnDefinition = "int8", nullable = false)
    private Long count;

}