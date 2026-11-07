package io.github.danielreker.smartpolls.dao.repositories.answers;

import io.github.danielreker.smartpolls.dao.entities.answers.TextAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextAnswerRepository
        extends JpaRepository<TextAnswerEntity, Long> {

    Long countByQuestionId(Long questionId);

}
