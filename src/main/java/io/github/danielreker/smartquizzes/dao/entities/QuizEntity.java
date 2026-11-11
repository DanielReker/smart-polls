package io.github.danielreker.smartquizzes.dao.entities;

import io.github.danielreker.smartquizzes.dao.entities.auth.UserEntity;
import io.github.danielreker.smartquizzes.dao.entities.questions.QuestionEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "quiz",
        indexes = @Index(name = "idx_quiz_owner_id", columnList = "owner_id")
)
@EntityListeners(AuditingEntityListener.class)
public class QuizEntity extends BaseEntity {

    @OneToMany(mappedBy = "quiz", orphanRemoval = true, cascade = CascadeType.ALL)
    @OrderBy("position ASC")
    private List<QuestionEntity> questions = new ArrayList<>();

    @OneToMany(mappedBy = "quiz", orphanRemoval = true, cascade = CascadeType.ALL)
    @OrderBy("createdDate ASC")
    private List<SubmissionEntity> submissions = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(50)", nullable = false, length = 50)
    private QuizStatus status;

    @Column(name = "name", columnDefinition = "varchar(255)", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "text", length = Length.LONG32)
    private String description;

    @CreatedDate
    @Column(name = "created_date", columnDefinition = "timestamptz", nullable = false)
    private Instant createdDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

}