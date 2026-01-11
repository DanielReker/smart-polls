package io.github.danielreker.smartpolls.dao.repositories.answers;

import io.github.danielreker.smartpolls.dao.entities.answers.TextAnswerTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TextAnswerTagRepository
        extends JpaRepository<TextAnswerTagEntity, Long> {

    @Query(value = """
        SELECT tat
        FROM TextAnswerTagEntity tat
        WHERE tat.answer.question.id = :textQuestionId
        """)
    List<TextAnswerTagEntity> findByTextQuestionId(Long textQuestionId);

}
