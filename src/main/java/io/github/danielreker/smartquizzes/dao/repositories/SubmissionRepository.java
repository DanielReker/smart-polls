package io.github.danielreker.smartquizzes.dao.repositories;

import io.github.danielreker.smartquizzes.dao.entities.SubmissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubmissionRepository
        extends JpaRepository<SubmissionEntity, UUID> {

    Long countByOwnerIdAndQuizId(UUID userId, UUID quizId);

    Long countByQuizId(UUID quizId);

}
