package io.github.danielreker.smartquizzes.dao.entities;

import io.github.danielreker.smartquizzes.dao.entities.answers.AnswerEntity;
import io.github.danielreker.smartquizzes.dao.entities.auth.UserEntity;
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
@Table(
        name = "submission",
        indexes = @Index(name = "idx_submission_quiz_id", columnList = "quiz_id")
)
@EntityListeners(AuditingEntityListener.class)
public class SubmissionEntity extends BaseEntity {

    @CreatedDate
    @Column(name = "created_date", columnDefinition = "timestamptz", nullable = false)
    private Instant createdDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    private QuizEntity quiz;

    @OneToMany(mappedBy = "submission", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<AnswerEntity> answers = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

}