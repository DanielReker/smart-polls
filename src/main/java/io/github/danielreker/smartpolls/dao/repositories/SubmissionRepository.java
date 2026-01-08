package io.github.danielreker.smartpolls.dao.repositories;

import io.github.danielreker.smartpolls.dao.entities.SubmissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<SubmissionEntity, Long> {
}
