package io.github.danielreker.smartpolls.dao.repositories.answers;

import io.github.danielreker.smartpolls.dao.entities.answers.MultiChoiceAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MultiChoiceAnswerRepository
        extends JpaRepository<MultiChoiceAnswerEntity, Long> {

    Long countByQuestionId(Long questionId);

    @Query(value = """
        SELECT count(mca)
        FROM MultiChoiceAnswerEntity mca
        JOIN mca.selectedChoices sc
        WHERE sc.id = :choiceId
        """)
    Long countChoiceSelectionsByChoiceId(Long choiceId);

}
