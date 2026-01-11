package io.github.danielreker.smartpolls.dao.repositories.questions;

import io.github.danielreker.smartpolls.dao.entities.questions.TextQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextQuestionRepository
        extends JpaRepository<TextQuestionEntity, Long> {
}
