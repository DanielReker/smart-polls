package io.github.danielreker.smartpolls.dao.repositories;

import io.github.danielreker.smartpolls.dao.entities.PollEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PollRepository
        extends JpaRepository<PollEntity, UUID> {

    boolean existsByIdAndOwnerId(UUID pollId, UUID userId);

    List<PollEntity> findByOwnerId(UUID id);
}
