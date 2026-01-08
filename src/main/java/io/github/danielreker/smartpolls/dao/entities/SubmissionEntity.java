package io.github.danielreker.smartpolls.dao.entities;

import io.github.danielreker.smartpolls.dao.entities.answers.AnswerEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "submission")
@EntityListeners(AuditingEntityListener.class)
public class SubmissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @CreatedDate
    @Column(name = "created_date", columnDefinition = "timestamptz", nullable = false)
    private Instant createdDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "poll_id", nullable = false)
    private PollEntity poll;

    @OneToMany(mappedBy = "submission", orphanRemoval = true, cascade = CascadeType.ALL)
    // TODO: Fix and enable back
    // @OrderBy("question.position ASC")
    private List<AnswerEntity> answers = new ArrayList<>();

}