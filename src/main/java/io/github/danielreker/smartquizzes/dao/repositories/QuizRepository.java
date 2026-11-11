package io.github.danielreker.smartquizzes.dao.repositories;

import io.github.danielreker.smartquizzes.dao.entities.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuizRepository
        extends JpaRepository<QuizEntity, UUID> {

    boolean existsByIdAndOwnerId(UUID quizId, UUID userId);

    List<QuizEntity> findByOwnerId(UUID id);
}
