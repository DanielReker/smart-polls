package io.github.danielreker.smartquizzes.dao.repositories.answers;

import io.github.danielreker.smartquizzes.dao.entities.answers.TextAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TextAnswerRepository
        extends JpaRepository<TextAnswerEntity, UUID> {

    Long countByQuestionId(UUID questionId);

}
