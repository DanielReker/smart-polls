package io.github.danielreker.smartpolls.dao.repositories;

import io.github.danielreker.smartpolls.dao.entities.PollEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<PollEntity, Long> {
}
