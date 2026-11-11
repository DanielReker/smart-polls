package io.github.danielreker.smartpolls.dao.repositories.answers;

import io.github.danielreker.smartpolls.dao.entities.answers.SingleChoiceAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface SingleChoiceAnswerRepository
        extends JpaRepository<SingleChoiceAnswerEntity, UUID> {

    Long countByQuestionId(UUID questionId);

    @Query(value = """
        SELECT count(mca)
        FROM SingleChoiceAnswerEntity mca
        WHERE mca.selectedChoice.id = :choiceId
        """)
    Long countChoiceSelectionsByChoiceId(UUID choiceId);

}
