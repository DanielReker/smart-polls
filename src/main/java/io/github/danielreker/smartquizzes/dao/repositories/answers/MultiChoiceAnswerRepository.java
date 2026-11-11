package io.github.danielreker.smartquizzes.dao.repositories.answers;

import io.github.danielreker.smartquizzes.dao.entities.answers.MultiChoiceAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface MultiChoiceAnswerRepository
        extends JpaRepository<MultiChoiceAnswerEntity, UUID> {

    Long countByQuestionId(UUID questionId);

    @Query(value = """
        SELECT count(mca)
        FROM MultiChoiceAnswerEntity mca
        JOIN mca.selectedChoices sc
        WHERE sc.id = :choiceId
        """)
    Long countChoiceSelectionsByChoiceId(UUID choiceId);

}
