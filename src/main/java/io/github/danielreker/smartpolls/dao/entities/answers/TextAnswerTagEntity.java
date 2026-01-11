package io.github.danielreker.smartpolls.dao.entities.answers;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "text_answer_tag")
public class TextAnswerTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "answer_id", nullable = false)
    private TextAnswerEntity answer;

    @Column(name = "name", columnDefinition = "varchar(255)", nullable = false)
    private String name;

    @Column(name = "embedding", columnDefinition = "vector(4096)", nullable = false)
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 4096)
    private float[] embedding;

}