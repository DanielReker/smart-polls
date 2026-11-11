package io.github.danielreker.smartpolls.dao.repositories;

import io.github.danielreker.smartpolls.dao.entities.SubmissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubmissionRepository
        extends JpaRepository<SubmissionEntity, UUID> {

    Long countByOwnerIdAndPollId(UUID userId, UUID pollId);

    Long countByPollId(UUID pollId);

}
