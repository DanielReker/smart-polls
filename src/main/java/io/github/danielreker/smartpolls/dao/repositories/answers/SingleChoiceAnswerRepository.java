package io.github.danielreker.smartpolls.dao.repositories.answers;

import io.github.danielreker.smartpolls.dao.entities.answers.SingleChoiceAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SingleChoiceAnswerRepository
        extends JpaRepository<SingleChoiceAnswerEntity, Long> {

    Long countByQuestionId(Long questionId);

    @Query(value = """
        SELECT count(mca)
        FROM SingleChoiceAnswerEntity mca
        WHERE mca.selectedChoice.id = :choiceId
        """)
    Long countChoiceSelectionsByChoiceId(Long choiceId);

}
