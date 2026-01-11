package io.github.danielreker.smartpolls.dao.repositories.questions;

import io.github.danielreker.smartpolls.dao.entities.questions.TextQuestionSummaryTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TextQuestionSummaryTagRepository
        extends JpaRepository<TextQuestionSummaryTagEntity, Long> {

    @Modifying
    @Query(value = """
        DELETE FROM TextQuestionSummaryTagEntity tqst
        WHERE tqst.question.id = :textQuestionId
        """)
    void deleteAllByTextQuestionId(Long textQuestionId);

}
