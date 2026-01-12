package io.github.danielreker.smartpolls.dao.repositories;

import io.github.danielreker.smartpolls.dao.entities.PollEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollRepository
        extends JpaRepository<PollEntity, Long> {

    boolean existsByIdAndOwnerId(Long pollId, Long userId);

    List<PollEntity> findByOwnerId(Long id);
}
