package io.github.danielreker.smartpolls.dao.repositories.answers;

import io.github.danielreker.smartpolls.dao.entities.answers.TextAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TextAnswerRepository
        extends JpaRepository<TextAnswerEntity, UUID> {

    Long countByQuestionId(UUID questionId);

}
